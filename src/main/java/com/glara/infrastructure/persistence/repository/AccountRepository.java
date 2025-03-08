package com.glara.infrastructure.persistence.repository;

import com.glara.domain.entity.Account;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

@ApplicationScoped
public class AccountRepository {

    @Inject
    Mutiny.SessionFactory sessionFactory;

    public Uni<Account> findById(Long id) {
        return sessionFactory.withSession(session -> session.find(Account.class, id));
    }

    public Uni<List<Account>> findAllAccounts() {
        return sessionFactory.withSession(session ->
                session.createQuery("FROM Account WHERE deleted = false", Account.class).getResultList());
    }

    public Uni<List<Account>> getAllAccountsByUserId(Long userId) {
        return sessionFactory.withSession(session -> session.createQuery(
                "SELECT a FROM Account a WHERE a.user.id = :userId AND a.deleted = false",
                Account.class).setParameter("userId", userId).getResultList());
    }

    public Uni<Account> update(Account account, Long id) {
        return sessionFactory.withTransaction(session ->
                session.find(Account.class, id)
                        .onItem().ifNotNull().transformToUni(existingAccount -> {
                            existingAccount.setName(account.getName());
                            existingAccount.setCurrentBalance(account.getCurrentBalance());
                            return session.merge(existingAccount)
                                    .onItem().transform(ignore -> existingAccount);
                        })
        );
    }

    public Uni<Account> createAccount(Account account) {
        return sessionFactory.withTransaction(session -> session.persist(account))
                .replaceWith(account);
    }

    public Uni<Boolean> deleteById(Long id) {
        return sessionFactory.withTransaction(session -> session.find(Account.class, id)
                .onItem().ifNotNull().invoke(account -> account.setDeleted(true))
                .replaceWith(Uni.createFrom().item(true))
        );
    }

    public Uni<Long> count() {
        return sessionFactory.withSession(session ->
                session.createSelectionQuery("SELECT COUNT(a) FROM Account a", Long.class)
                        .getSingleResult()
        );
    }

    public Uni<List<Account>> findAllPaginated(int page, int size) {
        return sessionFactory.withSession(session ->
                session.createSelectionQuery("FROM Account", Account.class)
                        .setFirstResult(page * size)
                        .setMaxResults(size)
                        .getResultList()
        );
    }
}
