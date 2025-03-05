package com.glara.application.service;

import com.glara.application.dto.CategoryDTO;
import com.glara.domain.entity.Category;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface CategoryService {
    Uni<CategoryDTO> getCategoryById(Long id);
    Uni<List<CategoryDTO>> getAllCategories();
    Uni<Void> createCategory(CategoryDTO categoryDTO);
    Uni<CategoryDTO> updateCategory(CategoryDTO categoryDTO, Long id);
    Uni<Integer> deleteCategoryById(Long id);
}
