package com.glara.domain.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

import java.util.Set;

@Schema(description = "Representa una cuenta bancaria asociada a un usuario.")
@Entity
@Table(name = "account", indexes = {
        @Index(name = "idx_account_user", columnList = "user_id"),
        @Index(name = "idx_account_type", columnList = "account_type_id"),
        @Index(name = "idx_account_name", columnList = "name")
})
@RegisterForReflection
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_type_id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_type"))
    private AccountType accountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_user"))
    @JsonIgnoreProperties({"accounts"})
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("account-subscription")
    private Set<Subscription> subscriptions;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("account-transaction")
    private Set<Transaction> transactions;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public Account(Long id, String name, AccountType accountType, User user, BigDecimal currentBalance) {
        this.id = id;
        this.name = name;
        this.accountType = accountType;
        this.user = user;
        this.currentBalance = currentBalance;
    }

    public Account() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
