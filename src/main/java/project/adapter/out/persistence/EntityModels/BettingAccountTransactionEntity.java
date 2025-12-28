package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;
import project.domain.model.Enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class BettingAccountTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private BigDecimal transactionAmmount;
    private BigDecimal accountBalanceAfterTransaction;
    private Instant createdAt;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private String description;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private BettingAccountEntity owner;

    protected BettingAccountTransactionEntity() {

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

    public void setOwner(BettingAccountEntity owner) {
        this.owner = owner;
    }

    public BettingAccountEntity getOwner() {
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

    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
