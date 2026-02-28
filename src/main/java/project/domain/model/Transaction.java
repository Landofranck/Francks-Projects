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
    private final Long betslipId;
    private Long ownerId;

    public Transaction(Money transactionAmmount, Money accountBalanceAfterTransaction, Instant createdAt, TransactionType transactionType, String description,Long betslipId) {
        this.transactionAmmount = transactionAmmount;
        this.accountBalanceAfterTransaction = accountBalanceAfterTransaction;
        this.createdAt = createdAt;
        this.type = transactionType;
        this.description = (description == null) ? "" : description;
        this.betslipId=betslipId;
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

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Long getBetslipId() {
        return betslipId;
    }
}
