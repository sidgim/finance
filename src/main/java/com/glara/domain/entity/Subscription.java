package com.glara.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;


enum FrequencyType {
    DAILY, WEEKLY, MONTHLY, YEARLY;
}

@Entity(name = "SubscriptionEntity")
@Table(name = "subscription", indexes = {
        @Index(name = "idx_subscription_account_id", columnList = "account_id"),
        @Index(name = "idx_subscription_subcategory_id", columnList = "subcategory_id"),
        @Index(name = "idx_subscription_name", columnList = "name"),
        @Index(name = "idx_subscription_frequency_type", columnList = "frequency_type"),
        @Index(name = "idx_subscription_amount", columnList = "amount"),
        @Index(name = "idx_subscription_next_payment", columnList = "next_payment")
})
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_subscription"))
    @JsonBackReference("account-subscription")
    private Account account;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="subcategory_id", nullable = false, foreignKey = @ForeignKey(name = "fk_subcategory_subscription"))
    private Subcategory subcategory;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column (name = "frequency_type", nullable = false, length = 20)
    private FrequencyType frequencyType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "next_payment", nullable = false)
    private LocalDate nextPaymentDate;


    public Subscription(long id, Account account, Subcategory subcategory, String name, FrequencyType frequencyType, BigDecimal amount, LocalDate nextPaymentDate) {
        this.id = id;
        this.account = account;
        this.subcategory = subcategory;
        this.name = name;
        this.frequencyType = frequencyType;
        this.amount = amount;
        this.nextPaymentDate = nextPaymentDate;
    }

    public Subscription() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FrequencyType getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(FrequencyType frequencyType) {
        this.frequencyType = frequencyType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getNextPaymentDate() {
        return nextPaymentDate;
    }

    public void setNextPaymentDate(LocalDate nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }
}
