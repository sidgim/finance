package com.glara.application.service.impl;

import com.glara.application.dto.AccountDTO;
import com.glara.application.mapper.AccountMapper;
import com.glara.application.service.AccountService;
import com.glara.domain.entity.Account;
import com.glara.infrastructure.persistence.repository.AccountRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = Logger.getLogger(AccountServiceImpl.class);

    @Inject
    AccountRepository accountRepository;

    @Inject
    AccountMapper accountMapper;

    @Override
    public Uni<AccountDTO> getAccount(Long id) {
        return accountRepository.findById(id)
                .onItem().ifNull().failWith(() -> new NotFoundException("Account not found"))
                .onItem().transform(accountMapper::toDTO)
                .onFailure().invoke(error -> LOGGER.error("Failed to getAccount: " + error.getMessage(), error));
    }

    @Override
    public Uni<List<AccountDTO>> findAllAccounts() {
        return accountRepository.findAllAccounts()
                .onItem().transform(accounts -> accounts.stream()
                        .map(accountMapper::toDTO)
                        .toList())
                .onFailure().recoverWithItem(List.of());
    }

    @Override
    public Uni<AccountDTO> createAccount(AccountDTO dto) {
        Account account = accountMapper.toEntity(dto);
        return accountRepository.createAccount(account)
                .replaceWith(account)
                .onItem().invoke(createdAccount -> LOGGER.infof("Account created successfully: %s", createdAccount.getId()))
                .onFailure().invoke(error -> LOGGER.error("Failed to createAccount: " + error.getMessage(), error))
                .map(accountMapper::toDTO);
    }

    @Override
    public Uni<AccountDTO> updateAccount(AccountDTO dto, Long id) {
        Account account = accountMapper.toEntity(dto);

        return accountRepository.update(account, id)
                .onItem().ifNull().failWith(() -> new NotFoundException("Account not found"))
                .onItem().transform(accountMapper::toDTO)
                .onFailure().invoke(error -> LOGGER.error("Failed to update account: " + error.getMessage(), error));
    }

    @Override
    public Uni<Void> deleteAccountById(Long id) {
        return accountRepository.findById(id)
                .onItem().ifNull().failWith(() -> new NotFoundException("Account not found"))
                .onItem().ifNotNull().call(existingAccount -> accountRepository.deleteById(id))
                .onFailure().invoke(error -> LOGGER.error("Error al eliminar la cuenta: " + error.getMessage(), error))
                .replaceWith(true)
                .onFailure().recoverWithNull().replaceWithVoid();
    }

    @Override
    public Uni<List<AccountDTO>> getAllAccountsByUserId(Long userId) {
        return accountRepository.getAllAccountsByUserId(userId)
                .onItem().transform(accounts -> accounts.stream()
                        .map(accountMapper::toDTO)
                        .toList())
                .onFailure().recoverWithItem(List.of());
    }

}
