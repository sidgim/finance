package com.glara.infrastructure.rest;


import com.glara.application.service.UserService;
import com.glara.domain.model.User;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Path;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    UserService userService;

    @GET
    @Path("/{id}")
    public Uni<Response> getUser(@PathParam("id") Long id) {
        return userService.getUser(id)
                .map(usuario -> Response.ok(usuario).build());
    }

    @GET
    public Uni<Response> getUsers() {
        return userService.getAllUsers()
                .map(usuarios -> Response.ok(usuarios).build());
    }

    @POST
    public Uni<Response> crearUsuario(User usuario) {
        return userService.createUser(usuario)
                .replaceWith(Response.status(Response.Status.CREATED).build());
    }

    @PUT
    public Uni<Response> actualizarUsuario(User usuario, @QueryParam("id") Long id) {
        return userService.updateUser(usuario,id)
                .replaceWith(Response.ok(usuario).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> eliminarUsuario(@PathParam("id") Long id) {
        return userService.deleteUserById(id)
                .replaceWith(Response.noContent().build());
    }
}
