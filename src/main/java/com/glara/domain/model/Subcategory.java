package com.glara.domain.model;


import jakarta.persistence.*;

@Entity
@Table(name = "subcategory", indexes = {
        @Index(name = "idx_subcategory_category_id", columnList = "category_id")
})
public class Subcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_subcategory_category"))
    private Category category;


    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length =  500)
    private String description;


    public Subcategory(Long id, Category category, String name, String description) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.description = description;
    }

    public Subcategory() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
