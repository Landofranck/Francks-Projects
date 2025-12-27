package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;
import project.domain.model.Enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class MomoAccountTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private BigDecimal transactionAmmount;
    private BigDecimal accountBalanceAfterTransaction;
    private Instant createdAt;
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private MobileMoneyAccountsEntity owner;

    protected MomoAccountTransactionEntity() {

    }

    public MomoAccountTransactionEntity(BigDecimal transactionAmmount, BigDecimal accountBalanceAfterTransaction, Instant createdAt, TransactionType transactionType) {

        this.transactionAmmount = transactionAmmount;
        this.accountBalanceAfterTransaction = accountBalanceAfterTransaction;
        this.createdAt = createdAt;
        this.type = transactionType;
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

}
