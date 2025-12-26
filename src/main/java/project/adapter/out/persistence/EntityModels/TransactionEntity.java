package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;
import project.domain.model.Account;
import project.domain.model.Enums.TransactionType;
import project.domain.model.Money;

import java.time.Instant;

@Entity
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Money transactionAmmount;
    private Money accountBalanceAfterTransaction;
    private Instant createdAt;
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private AccountEntity owner;

    protected TransactionEntity() {

    }

    public TransactionEntity(Money transactionAmmount, Money accountBalanceAfterTransaction, Instant createdAt, TransactionType transactionType) {

        this.transactionAmmount = transactionAmmount;
        this.accountBalanceAfterTransaction = accountBalanceAfterTransaction;
        this.createdAt = createdAt;
        this.type = transactionType;
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

    public void setOwner(AccountEntity owner) {
        this.owner = owner;
    }

    public AccountEntity getOwner() {
        return owner;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setAccountBalanceAfterTransaction(Money accountBalanceAfterTransaction) {
        this.accountBalanceAfterTransaction = accountBalanceAfterTransaction;
    }

    public void setTransactionAmmount(Money transactionAmmount) {
        this.transactionAmmount = transactionAmmount;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

}
