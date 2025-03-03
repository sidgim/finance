package com.glara.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;


enum TransactionType {
    INGRESO, GASTO;
}

@Entity
@Table(name = "transaction", indexes = {
        @Index(name = "idx_transaction_account_id", columnList = "account_id"),
        @Index(name = "idx_transaction_subcategory_id", columnList = "subcategory_id"),
        @Index(name = "idx_transaction_fecha", columnList = "fecha"),
        @Index(name = "idx_transaction_monto", columnList = "monto"),
        @Index(name = "idx_transaction_transaction_type", columnList = "transaction_type")
})
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_transaction_account"))
    private Account account;

    @ManyToOne
    @JoinColumn(name = "subcategory_id", foreignKey = @ForeignKey(name = "fk_transaction_subcategory"))
    private Subcategory subcategoria;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    private TransactionType transactionType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Subcategory getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(Subcategory subcategoria) {
        this.subcategoria = subcategoria;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
