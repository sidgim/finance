package com.glara.domain.repository;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

public abstract class BaseRepository<T, ID> {

    @Inject
    protected Mutiny.SessionFactory sessionFactory;

    private final Class<T> entityType;

    public BaseRepository(Class<T> entityType) {
        this.entityType = entityType;
    }

    public Uni<T> findById(ID id) {
        return sessionFactory.withSession(session ->
                session.find(entityType, id)
                        .onItem().ifNull().failWith(() -> new WebApplicationException(entityType.getSimpleName() + " no encontrado"))
        );
    }

    public Uni<List<T>> findAll() {
        return sessionFactory.withSession(session ->
                session.createSelectionQuery("FROM " + entityType.getSimpleName(), entityType)
                        .getResultList()
        );
    }

    public Uni<List<T>> findAllPaginated(int page, int size) {
        return sessionFactory.withSession(session ->
                session.createSelectionQuery("FROM " + entityType.getSimpleName(), entityType)
                        .setFirstResult(page * size)
                        .setMaxResults(size)
                        .getResultList()
        );
    }

    public Uni<Void> persist(T entity) {
        return sessionFactory.withTransaction(session -> session.persist(entity));
    }

    public Uni<T> update(T updatedEntity, ID id) {
        return sessionFactory.withTransaction(session ->
                session.find(entityType, id)
                        .onItem().ifNotNull().invoke(existingEntity -> updateEntity(existingEntity, updatedEntity))
                        .onItem().ifNull().failWith(() -> new NotFoundException(entityType.getSimpleName() + " no encontrado"))
        );
    }

    public Uni<Integer> deleteById(ID id) {
        return sessionFactory.withTransaction(session ->
                session.createMutationQuery("DELETE FROM " + entityType.getSimpleName() + " WHERE id = :id")
                        .setParameter("id", id)
                        .executeUpdate()
        );
    }

    // Método abstracto que las subclases pueden sobrescribir para definir cómo actualizar
    protected void updateEntity(T existingEntity, T updatedEntity) {
    }
}
