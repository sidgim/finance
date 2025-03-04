package com.glara.domain.repository;

import com.glara.application.dto.UserDTO;
import com.glara.domain.model.User;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

@ApplicationScoped
public class UserRepository {

    @Inject
    Mutiny.SessionFactory sessionFactory;


    public Uni<User> findById(Long id) {
        return sessionFactory.withSession(session ->
                session.find(User.class, id)
                        .onItem().call(user -> user != null ? session.fetch(user.getAccounts()) : Uni.createFrom().nullItem())
        );
    }


    public Uni<List<UserDTO>> findAll() {
        return sessionFactory.withSession(session ->
                session.createSelectionQuery("FROM User", User.class)
                        .getResultList()
                        .map(users -> users.stream()
                                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                                .toList()
                        )
        );
    }

    public Uni<List<UserDTO>> findAllPaginated(int page, int size) {
        return sessionFactory.withSession(session ->
                session.createSelectionQuery("FROM User", User.class)
                        .setFirstResult(page * size)
                        .setMaxResults(size)
                        .getResultList()
                        .map(users -> users.stream()
                                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                                .toList()
                        )
        );
    }


    public Uni<User> findByEmail(String email) {
        return sessionFactory.withSession(session ->
                session.createSelectionQuery("FROM User WHERE email = :email", User.class)
                        .setParameter("email", email)
                        .getSingleResultOrNull()
                        .onItem().ifNull().failWith(() -> new IllegalArgumentException("Usuario no encontrado"))
        );
    }


    public Uni<Void> persist(User user) {
        return sessionFactory.withTransaction(session -> session.persist(user));
    }


    public Uni<User> update(User updatedUser, Long id) {
        return sessionFactory.withTransaction(session ->
                session.find(User.class, id)
                        .onItem().ifNotNull().invoke(user -> {
                            user.setName(updatedUser.getName());
                            user.setEmail(updatedUser.getEmail());
                        })
                        .onItem().ifNull().failWith(() -> new NotFoundException("Usuario no encontrado"))
        );
    }

    public Uni<Integer> deleteById(Long id) {
        return sessionFactory.withTransaction(session ->
                session.createMutationQuery("DELETE FROM User WHERE id = :id")
                        .setParameter("id", id)
                        .executeUpdate()
        );
    }
}
