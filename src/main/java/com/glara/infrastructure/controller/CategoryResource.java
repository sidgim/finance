package com.glara.infrastructure.controller;

import com.glara.application.dto.CategoryDTO;
import com.glara.application.service.CategoryService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/category")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @Inject
    CategoryService categoryService;

    @GET
    public Uni<List<CategoryDTO>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GET
    @Path("/{id}")
    public Uni<CategoryDTO> getCategoryById(@PathParam("id") Long id) {
        return categoryService.getCategoryById(id);
    }

    @POST
    public Uni<CategoryDTO> createCategory(@Valid CategoryDTO categoryDTO) {
        return categoryService.createCategory(categoryDTO);
    }

    @PUT
    @Path("/{id}")
    public Uni<CategoryDTO> updateCategory(@PathParam("id") Long id, @Valid CategoryDTO categoryDTO) {
        return categoryService.updateCategory(categoryDTO, id);
    }

    @DELETE
    @Path("/{id}")
    public Uni<Void> deleteCategoryById(@PathParam("id") Long id) {
        return categoryService.deleteCategoryById(id);
    }
}
