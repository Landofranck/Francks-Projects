package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;
import project.domain.model.Enums.AccountType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MobileMoneyAccountsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public AccountType accountType;
    private BigDecimal accountBalance;
    private Boolean dailyLimit;
    private Boolean weeklyLimit;
    private Boolean monthlyLimit;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MomoAccountTransactionEntity> transactionHistory=new ArrayList<>();

    public MobileMoneyAccountsEntity() {
    }

    public MobileMoneyAccountsEntity(AccountType accountType) {
        this.accountType = accountType;
        this.accountBalance = BigDecimal.ZERO;
        this.dailyLimit = false;
        this.weeklyLimit = false;
        this.monthlyLimit = false;
    }

    public void addBettingaccoutTransactionEntity(MomoAccountTransactionEntity transaction) {
        this.transactionHistory.add(transaction);
        transaction.setOwner(this);
    }

    public void setTransactionEntityHistory(List<MomoAccountTransactionEntity> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public List<MomoAccountTransactionEntity> getTransactionHistory() {
        return transactionHistory;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public Boolean getDailyLimit() {
        return dailyLimit;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
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

    public Long getId() {
        return id;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

}
