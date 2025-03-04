package com.glara.application.service;

import com.glara.application.dto.AccountDTO;
import com.glara.domain.model.Account;
import com.glara.domain.repository.AccountRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;

@ApplicationScoped
public class AccountService {
    @Inject
    private AccountRepository accountRepository;


    public Uni<Account> getAccount(Long id) {
        return accountRepository.findById(id)
                .onItem().ifNull().failWith(() -> new WebApplicationException("Account not found", 404)); // ✅ Return 404 if null
    }


    public Uni<List<Account>> findAllAccounts() {
        return accountRepository.findAllAccounts();
    }

    public Uni<Void> createAccount(AccountDTO usuario) {
        return accountRepository.createAccount(usuario);
    }

    public Uni<Void> updateAccount(Account usuario, Long id) {
        return accountRepository.update(usuario, id);
    }

    public Uni<Boolean> deleteAccountById(Long id) {
        return accountRepository.deleteById(id);
    }

}
