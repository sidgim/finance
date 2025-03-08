package com.glara.infrastructure.persistence.repository;

import com.glara.domain.entity.Category;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

@ApplicationScoped
public class CategoryRepository {

    @Inject
    Mutiny.SessionFactory sessionFactory;


    public Uni<Category> persist(Category category) {
        return sessionFactory.withTransaction(session -> session.persist(category).replaceWith(category));
    }


    public Uni<Category> findById(Long id) {
        return sessionFactory.withSession(session ->
                session.find(Category.class, id)
                        .onItem().ifNull().failWith(() -> new NotFoundException("Category not found"))
        );
    }

    public Uni<Category> findByName(String name) {
        return sessionFactory.withSession(session ->
                session.createSelectionQuery("FROM Category WHERE name = :name", Category.class)
                        .setParameter("name", name)
                        .getSingleResultOrNull()
        );
    }

    public Uni<List<Category>> findAll() {
        return sessionFactory.withSession(session ->
                session.createSelectionQuery("FROM Category", Category.class)
                        .getResultList()
        );
    }

    public Uni<List<Category>> findAllPaginated(int page, int size) {
        return sessionFactory.withSession(session ->
                session.createSelectionQuery("FROM Category", Category.class)
                        .setFirstResult(page * size)
                        .setMaxResults(size)
                        .getResultList()
        );
    }

    public Uni<Category> update(Category updatedCategory, Long id) {
        return sessionFactory.withTransaction(session ->
                session.find(Category.class, id)
                        .onItem().ifNotNull().invoke(category -> {
                            category.setName(updatedCategory.getName());
                            category.setDescription(updatedCategory.getDescription());
                        })
                        .onItem().ifNull().failWith(() -> new NotFoundException("Category not found"))
        );
    }

    public Uni<Integer> deleteById(Long id) {
        return sessionFactory.withTransaction(session ->
                session.createMutationQuery("DELETE FROM Category WHERE id = :id")
                        .setParameter("id", id)
                        .executeUpdate()
        );
    }
}
