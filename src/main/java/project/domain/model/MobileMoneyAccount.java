package project.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import project.domain.model.Enums.AccountType;
import project.domain.model.Enums.TransactionType;

public class MobileMoneyAccount implements Account {
    private Long id;
    public final AccountType accountType;
    private Money accountBalance;
    private Boolean dailyLimit;
    private Boolean weeklyLimit;
    private Boolean monthlyLimit;
    private List<Transaction> transactionHistory;

    public MobileMoneyAccount(AccountType accountType) {
        this.accountType = accountType;
        this.accountBalance = new Money(BigDecimal.ZERO);
        this.dailyLimit = false;
        this.weeklyLimit = false;
        this.monthlyLimit = false;
    }

    public void addTransaction(Transaction transaction) {
        transaction.setOwner(this);
        this.transactionHistory.add(transaction);

    }

    public void deposit(Money money, Instant createdAt) {
        this.accountBalance.add(money);
        addTransaction(new Transaction(money, new Money(accountBalance.getValue()), createdAt, TransactionType.DEPOSIT));

    }

    public void withdraw(Money money) {
        if (!this.accountBalance.isGreaterThan(money)) {
            throw new RuntimeException("you cannot make withdrwal of " + money.getValue());
        }
        accountBalance.subtract(money);
        addTransaction(new Transaction(money, new Money(accountBalance.getValue()), Instant.now(), TransactionType.WITHDRAWAL));
    }

    public void setTransactionHistory(List<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public List<Transaction> getTransactionHistory() {
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

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
