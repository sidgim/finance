package com.glara.infrastructure.persistence.repository;

import com.glara.domain.entity.AccountType;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

@ApplicationScoped
public class AccountTypeRepository {

    @Inject
    Mutiny.SessionFactory sessionFactory;

    public Uni<List<AccountType>> findAll() {
        return sessionFactory.withSession(session ->
             session.createSelectionQuery("FROM AccountType", AccountType.class).getResultList()
        );
    }

    public Uni<AccountType> findById(Long id) {
        return sessionFactory.withSession(session -> session.find(AccountType.class, id))
                .onItem().ifNull().failWith(() -> new WebApplicationException("Tipo de cuenta no encontrada", 404));
    }

    public Uni<Void> persist(AccountType accountType) {
        return sessionFactory.withTransaction(session -> session.persist(accountType));
    }

    public Uni<AccountType> update (AccountType accountType) {
        return sessionFactory.withTransaction(session -> session.find(AccountType.class, accountType.getId())
                .onItem().ifNotNull().invoke(account -> {
                    account.setName(accountType.getName());
                })
        );
    }

    public Uni<Integer> deleteById(Long id) {
        return sessionFactory.withTransaction(session ->
                session.createMutationQuery("DELETE FROM AccountType WHERE id = :id")
                        .setParameter("id", id)
                        .executeUpdate()
        );
    }


}
