package project.domain.model;

import project.domain.model.Enums.TransactionType;

import java.time.Instant;

public class Transaction {
    private long id;
    private final Money transactionAmmount;
    private final Money accountBalanceAfterTransaction;
    private final Instant createdAt;
    private final TransactionType type;
    private final String description;

    private Account owner;

    public Transaction(Money transactionAmmount, Money accountBalanceAfterTransaction, Instant createdAt, TransactionType transactionType, String description) {
        this.transactionAmmount = transactionAmmount;
        this.accountBalanceAfterTransaction = accountBalanceAfterTransaction;
        this.createdAt = createdAt;
        this.type = transactionType;
        this.description = (description == null) ? "" : description;
    }

    public long getId() {
        return id;
    }

    public Money getAccountBalanceAfterTransaction() {
        return accountBalanceAfterTransaction;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public TransactionType getType() {
        return type;
    }

    public Money getTransactionAmmount() {
        return transactionAmmount;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public Account getOwner() {
        return owner;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }
}
