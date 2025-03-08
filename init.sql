-- Crear base de datos si no existe
CREATE DATABASE finance_db;

-- Conectar a la base de datos
\c finance_db;

-- Crear esquemas
CREATE SCHEMA IF NOT EXISTS public;

-- Crear usuarios específicos para cada microservicio
CREATE USER user_service WITH PASSWORD 'user_pass';
CREATE USER account_service WITH PASSWORD 'account_pass';
CREATE USER transaction_service WITH PASSWORD 'transaction_pass';
CREATE USER category_service WITH PASSWORD 'category_pass';

-- Dar permisos a cada usuario solo en sus tablas correspondientes
GRANT CONNECT ON DATABASE finance_db TO user_service, account_service, transaction_service, category_service;

GRANT USAGE ON SCHEMA public TO user_service, account_service, transaction_service, category_service;

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE users TO user_service;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE account, account_type TO account_service;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE transaction TO transaction_service;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE category, subcategory TO category_service;
