package com.glara.domain.repository;

import com.glara.application.dto.AccountDTO;
import com.glara.domain.model.Account;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

@ApplicationScoped
public class AccountRepository {

    @Inject
    Mutiny.SessionFactory sessionFactory;

    public Uni<Account> findById(Long id) {
        return sessionFactory.withSession(session -> session.createQuery(
                        "SELECT a FROM Account a " +
                                "LEFT JOIN FETCH a.user u " +
                                "LEFT JOIN FETCH u.accounts " +
                                "LEFT JOIN FETCH a.accountType " +
                                "LEFT JOIN FETCH a.subscriptions " +
                                "LEFT JOIN FETCH a.transactions " +
                                "WHERE a.id = :id AND a.deleted = false",
                        Account.class
                ).setParameter("id", id)
                .getSingleResultOrNull());
    }

    public Uni<List<Account>> findAllAccountsByUserId(Long userId) {
        return sessionFactory.withSession(session ->
                session.createQuery(
                                "SELECT a FROM Account a " +
                                        "LEFT JOIN FETCH a.user u " +
                                        "LEFT JOIN FETCH a.accountType " +
                                        "LEFT JOIN FETCH a.subscriptions " +
                                        "LEFT JOIN FETCH a.transactions " +
                                        "WHERE u.id = :userId AND a.deleted = false",
                                Account.class
                        ).setParameter("userId", userId)
                        .getResultList()
        );
    }

    public Uni<List<Account>> findAllAccounts() {
        return sessionFactory.withSession(session ->
                session.createQuery(
                        "SELECT a FROM Account a " +
                                "LEFT JOIN FETCH a.user u " +  // Cargar el usuario
                                "LEFT JOIN FETCH a.accountType " +
                                "LEFT JOIN FETCH a.subscriptions " +
                                "LEFT JOIN FETCH a.transactions " +
                                "WHERE a.deleted = false",
                        Account.class
                ).getResultList()
        );
    }


    public Uni<Void> update(Account account, Long id) {
        return sessionFactory.withTransaction(session -> session.find(Account.class, id)
                .onItem().ifNotNull().invoke(existingAccount -> {
                    existingAccount.setName(account.getName());
                    existingAccount.setCurrentBalance(account.getCurrentBalance());
                })
                .onItem().ifNull().failWith(() -> new WebApplicationException("Account not found", 404))
        ).replaceWithVoid();
    }


    public Uni<Void> createAccount(AccountDTO dto) {
        return sessionFactory.withTransaction(session ->
                session.createNativeQuery(
                                "INSERT INTO account (name, currentbalance, account_type_id, user_id) " +
                                        "VALUES (:name, :currentBalance, :accountType, :userId)")
                        .setParameter("name", dto.name())
                        .setParameter("currentBalance", dto.currentBalance())
                        .setParameter("accountType", dto.accountTypeId())
                        .setParameter("userId", dto.userId())
                        .executeUpdate()
        ).replaceWithVoid();
    }


    public Uni<Boolean> deleteById(Long id) {
        return sessionFactory.withTransaction(session ->
                session.find(Account.class, id)
                        .onItem().ifNull().failWith(() -> new WebApplicationException("Account not found", 404))
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
