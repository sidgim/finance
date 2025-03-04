package com.glara.domain.repository;

import com.glara.domain.model.Category;

public class CategoryRepository extends BaseRepository<Category, Long> {
    public CategoryRepository() {
        super(Category.class);
    }
}
