package com.glara.application.service;

import com.glara.application.dto.AccountDTO;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface AccountService {

    Uni<AccountDTO> getAccount(Long id);

    Uni<List<AccountDTO>> findAllAccounts();

    Uni<AccountDTO> createAccount(AccountDTO accountDTO);

    Uni<AccountDTO> updateAccount(AccountDTO accountDTO, Long id);

    Uni<Void> deleteAccountById(Long id);

    Uni<List<AccountDTO>> getAllAccountsByUserId(Long userId);
}
