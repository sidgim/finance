package com.glara.application.service;

import com.glara.application.dto.CategoryDTO;
import io.smallrye.mutiny.Uni;
import java.util.List;

public interface CategoryService {
    Uni<List<CategoryDTO>> getAllCategories();
    Uni<CategoryDTO> getCategoryById(Long id);
    Uni<CategoryDTO> createCategory(CategoryDTO categoryDTO);
    Uni<CategoryDTO> updateCategory(CategoryDTO categoryDTO, Long id);
    Uni<Void> deleteCategoryById(Long id);
}
