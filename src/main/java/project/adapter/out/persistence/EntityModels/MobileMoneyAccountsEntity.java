package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;
import project.domain.model.Enums.AccountType;
import project.domain.model.Enums.TransactionType;
import project.domain.model.Money;
import project.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
public class MobileMoneyAccountsEntity implements AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public AccountType accountType;
    private Money accountBalance;
    private Boolean dailyLimit;
    private Boolean weeklyLimit;
    private Boolean monthlyLimit;
    @OneToMany(mappedBy = "MobileMoneyAccountsEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity> transactionHistory;

    public MobileMoneyAccountsEntity() {
    }

    public MobileMoneyAccountsEntity(AccountType accountType) {
        this.accountType = accountType;
        this.accountBalance = new Money(BigDecimal.ZERO);
        this.dailyLimit = false;
        this.weeklyLimit = false;
        this.monthlyLimit = false;
    }

    public void addTransactionEntity(TransactionEntity transaction) {
        this.transactionHistory.add(transaction);
        transaction.setOwner(this);
    }

    public void setTransactionEntityHistory(List<TransactionEntity> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public List<TransactionEntity> getTransactionHistory() {
        return transactionHistory;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Money getAccountBalance() {
        return accountBalance;
    }

    public Boolean getDailyLimit() {
        return dailyLimit;
    }

    public void setAccountBalance(Money accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Boolean getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setDailyLimit(Boolean dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public Boolean getWeeklyLimit() {
        return weeklyLimit;
    }

    public void setWeeklyLimit(Boolean weeklyLimit) {
        this.weeklyLimit = weeklyLimit;
    }

    public void setMonthlyLimit(Boolean monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    @Override
    public Long getAccountId() {
        return this.id;
    }

    public Long getId() {
        return id;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

}
