package com.glara.application.service.impl;

import com.glara.application.dto.CategoryDTO;
import com.glara.application.mapper.CategoryMapper;
import com.glara.application.service.CategoryService;
import com.glara.infrastructure.persistence.repository.CategoryRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOG = Logger.getLogger(CategoryServiceImpl.class);

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
                .onItem().ifNull().failWith(() -> {
                    LOG.warnf("Category with ID %d not found", id);
                    return new WebApplicationException("Category not found", 404);
                })
                .map(categoryMapper::toDTO);
    }

    @Override
    public Uni<CategoryDTO> createCategory(CategoryDTO categoryDTO) {
        return categoryRepository.findByName(categoryDTO.name())
                .onItem().ifNotNull().failWith(() -> new WebApplicationException("Category already exists", 409))
                .replaceWith(categoryMapper.toEntity(categoryDTO))
                .flatMap(categoryRepository::persist)
                .map(categoryMapper::toDTO);
    }

    @Override
    public Uni<CategoryDTO> updateCategory(CategoryDTO categoryDTO, Long id) {
        return categoryRepository.findById(id)
                .onItem().ifNull().failWith(() -> {
                    LOG.warnf("Category with ID %d not found for update", id);
                    return new WebApplicationException("Category not found", 404);
                })
                .flatMap(existingCategory -> {
                    existingCategory.setName(categoryDTO.name());
                    existingCategory.setDescription(categoryDTO.description());
                    return categoryRepository.persist(existingCategory)
                            .onItem().invoke(() -> LOG.infof("Category ID %d updated successfully", id))
                            .map(categoryMapper::toDTO);
                });
    }

    @Override
    public Uni<Void> deleteCategoryById(Long id) {
        return categoryRepository.findById(id)
                .onItem().ifNull().failWith(() -> {
                    LOG.warnf("Category with ID %d not found for deletion", id);
                    return new WebApplicationException("Category not found", 404);
                })
                .flatMap(category -> categoryRepository.deleteById(id))
                .replaceWithVoid()
                .onItem().invoke(() -> LOG.infof("Category ID %d deleted successfully", id));
    }
}
