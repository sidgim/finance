package com.glara.infrastructure.rest;

import com.glara.application.dto.AccountDTO;
import com.glara.application.service.AccountService;
import com.glara.domain.model.Account;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {
    @Inject
    AccountService accountService;

    @GET
    @Path("/{id}")
    public Uni<Response> getAccountById(@PathParam("id") Long id) {
        return accountService.getAccount(id)
                .map(account -> Response.ok(account).build());
    }

    @GET
    public Uni<Response> getAllAccount() {
        return accountService.findAllAccounts()
                .map(account -> Response.ok(account).build());
    }

    @POST
    public Uni<Response> createAccount(AccountDTO accountDTO) {
        return accountService.createAccount(accountDTO)
                .replaceWith(Response.status(Response.Status.CREATED).build());
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateAccount(@PathParam("id") Long id, Account account) {
        return accountService.updateAccount(account, id)
                .replaceWith(Response.status(Response.Status.OK).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> deleteAccountById(@PathParam("id") Long id) {
        return accountService.deleteAccountById(id)
                .map(deleted -> deleted ? Response.noContent().build() : Response.status(404).build());
    }
}
