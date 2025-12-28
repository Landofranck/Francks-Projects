package project.adapter.in.web.MobileMoneyDto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import project.adapter.in.web.TransactionDTO.TransactionDto;
import project.adapter.out.persistence.EntityModels.MomoAccountTransactionEntity;
import project.domain.model.Enums.AccountType;
import project.domain.model.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReadMomoAccountDto {
    private Long id;
    private AccountType accountType;
    private BigDecimal accountBalance;
    private Boolean dailyLimit;
    private Boolean weeklyLimit;
    private Boolean monthlyLimit;
    private List<TransactionDto> transactionHistory;


    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMonthlyLimit(Boolean monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public void setDailyLimit(Boolean dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public void setWeeklyLimit(Boolean weeklyLimit) {
        this.weeklyLimit = weeklyLimit;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public Boolean getDailyLimit() {
        return dailyLimit;
    }

    public Boolean getWeeklyLimit() {
        return weeklyLimit;
    }

    public Boolean getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setTransactionHistory(List<TransactionDto> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public List<TransactionDto> getTransactionHistory() {
        return transactionHistory;
    }
}
