package com.glara.application.service;

import com.glara.application.dto.CategoryDTO;
import com.glara.application.mapper.CategoryMapper;
import com.glara.domain.entity.Category;
import com.glara.infrastructure.persistence.repository.CategoryRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategoryServiceImpl implements CategoryService {

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    CategoryMapper categoryMapper;

    @Override
    public Uni<List<CategoryDTO>> getAllCategories() {
        return categoryRepository.findAll()
                .map(categories -> categories.stream()
                        .map(categoryMapper::toDTO)
                        .collect(Collectors.toList()));
    }


    @Override
    public Uni<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDTO);
    }

    @Override
    public Uni<Void> createCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);
        return categoryRepository.persist(category)
                .onFailure().invoke(error -> {
                    System.err.println("Error al crear categoría: " + error.getMessage());
                })
                .onFailure().recoverWithUni(error -> Uni.createFrom().failure(new RuntimeException("No se pudo crear la categoría")));
    }


    @Override
    public Uni<CategoryDTO> updateCategory(CategoryDTO categoryDTO, Long id) {
        return categoryRepository.findById(id)
                .onItem().ifNotNull().transformToUni(existingCategory -> {
                    existingCategory.setName(categoryDTO.name());
                    existingCategory.setDescription(categoryDTO.description());
                    return categoryRepository.persist(existingCategory)
                            .replaceWith(categoryMapper.toDTO(existingCategory));
                });
    }

    @Override
    public Uni<Integer> deleteCategoryById(Long id) {
        return categoryRepository.deleteById(id);
    }
}
