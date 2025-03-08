package com.glara.infrastructure.controller;

import com.glara.application.dto.AccountDTO;
import com.glara.application.service.impl.AccountServiceImpl;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;


@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {
    @Inject
    AccountServiceImpl accountService;

    @GET
    @Path("/{id}")
    public Uni<AccountDTO> getAccountById(@PathParam("id") Long id) {
        return accountService.getAccount(id);
    }

    @GET
    public Uni<List<AccountDTO>> getAllAccount() {
        return accountService.findAllAccounts();
    }

    @POST
    public Uni<Response> createAccount(AccountDTO accountDTO) {
        return accountService.createAccount(accountDTO).map(account -> Response.created(URI.create("/account/" + account.id()))
                .entity(account)
                .build()
        );
    }

    @PUT
    @Path("/{id}")
    public Uni<AccountDTO> updateAccount(@PathParam("id") Long id, AccountDTO account) {
        return accountService.updateAccount(account, id);
    }

    @DELETE
    @Path("/{id}")
    public Uni<Void> deleteAccountById(@PathParam("id") Long id) {
        return accountService.deleteAccountById(id);
    }

    @GET
    @Path("/user/{userId}")
    public Uni<List<AccountDTO>> getAllAccountByUserId(@PathParam("userId") Long userId) {
        return accountService.getAllAccountsByUserId(userId);
    }

}
