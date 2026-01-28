package project.adapter.out.persistence.EntityModels.MomoEntites;

import jakarta.persistence.*;
import project.domain.model.Enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class MomoAccountTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String description;
    private BigDecimal transactionAmmount;
    private BigDecimal accountBalanceAfterTransaction;
    private Instant createdAt;
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private MobileMoneyAccountsEntity owner;

    public MomoAccountTransactionEntity() {

    }



    public long getId() {
        return id;
    }

    public BigDecimal getAccountBalanceAfterTransaction() {
        return accountBalanceAfterTransaction;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getTransactionAmmount() {
        return transactionAmmount;
    }

    public void setOwner(MobileMoneyAccountsEntity owner) {
        this.owner = owner;
    }

    public MobileMoneyAccountsEntity getOwner() {
        return owner;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setAccountBalanceAfterTransaction(BigDecimal accountBalanceAfterTransaction) {
        this.accountBalanceAfterTransaction = accountBalanceAfterTransaction;
    }

    public void setTransactionAmmount(BigDecimal transactionAmmount) {
        this.transactionAmmount = transactionAmmount;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
