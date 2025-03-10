package com.glara.domain.entity;

import jakarta.persistence.*;

@Entity(name = "AccountTypeEntity")
@Table(name = "account_type")
public class AccountType{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    public AccountType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public AccountType() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
