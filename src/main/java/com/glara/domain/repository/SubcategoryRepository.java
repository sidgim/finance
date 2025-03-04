package com.glara.domain.repository;

import com.glara.domain.model.Subcategory;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class SubcategoryRepository extends BaseRepository<Subcategory, Long> {
    public SubcategoryRepository() {
        super(Subcategory.class);
    }
}
