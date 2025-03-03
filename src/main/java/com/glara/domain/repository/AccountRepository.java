package com.glara.domain.repository;

import com.glara.domain.model.Account;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

@ApplicationScoped
public class AccountRepository {

    @Inject
    Mutiny.SessionFactory sessionFactory;

    // 🔍 Obtener cuenta por ID de forma segura
    public Uni<Account> findById(Long id) {
        return sessionFactory.withSession(session -> session.find(Account.class, id))
                .onItem().ifNull().failWith(() -> new IllegalArgumentException("Cuenta inexistente"));
    }

    // 🔍 Obtener todas las cuentas
    public Uni<List<Account>> findAll() {
        return sessionFactory.withSession(session ->
                session.createSelectionQuery("FROM Account", Account.class)
                        .getResultList()
        );
    }

    // 🔍 Buscar cuentas por usuario
    public Uni<List<Account>> findByUserId(Long userId) {
        return sessionFactory.withSession(session ->
                session.createSelectionQuery("FROM Account a WHERE a.user.id = :userId", Account.class)
                        .setParameter("userId", userId)
                        .getResultList()
        );
    }

    // ✏️ Actualizar cuenta (nombre y saldo) de forma segura
    public Uni<Account> update(Account updatedAccount) {
        return sessionFactory.withTransaction(session ->
                session.find(Account.class, updatedAccount.getId())
                        .onItem().ifNotNull().invoke(account -> {
                            account.setName(updatedAccount.getName());
                            account.setCurrentBalance(updatedAccount.getCurrentBalance());
                        })
                        .onItem().ifNull().failWith(() -> new IllegalArgumentException("Cuenta no encontrada"))
        );
    }

    // ➕ Guardar cuenta
    public Uni<Void> persist(Account account) {
        return sessionFactory.withTransaction(session -> session.persist(account));
    }

    // ❌ Eliminar cuenta por ID
    public Uni<Integer> deleteById(Long id) {
        return sessionFactory.withTransaction(session ->
                session.createMutationQuery("DELETE FROM Account WHERE id = :id")
                        .setParameter("id", id)
                        .executeUpdate()
        );
    }

    // 🔢 Contar total de cuentas
    public Uni<Long> count() {
        return sessionFactory.withSession(session ->
                session.createSelectionQuery("SELECT COUNT(a) FROM Account a", Long.class)
                        .getSingleResult()
        );
    }

    // 📜 Obtener cuentas con paginación optimizada
    public Uni<List<Account>> findAllPaginated(int page, int size) {
        return sessionFactory.withSession(session ->
                session.createSelectionQuery("FROM Account", Account.class)
                        .setFirstResult(page * size)
                        .setMaxResults(size)
                        .getResultList()
        );
    }
}
