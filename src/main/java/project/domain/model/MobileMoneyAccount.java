package project.domain.model;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.ws.rs.BadRequestException;
import project.adapter.in.web.Utils.Code;
import project.application.error.AccountLimitException;
import project.application.error.InsufficientFundsException;
import project.domain.model.Enums.MomoAccountType;
import project.domain.model.Enums.TransactionType;

public class MobileMoneyAccount implements Account {
    private Long id;
    private final String name;
    public final MomoAccountType accountType;
    private Money accountBalance;
    private Money dailyLimitAmount;
    private Money weeklyLimitAmount;
    private Money monthlyLimitAmount;
    private Instant updatedAt;
    private final List<Transaction> transactionHistory;

    public MobileMoneyAccount(Long id, MomoAccountType accountType, String name, BigDecimal dailyLimitAmount, BigDecimal weeklyLimitAmount, BigDecimal monthlyLimitAmount) {
        this.id = id;
        this.updatedAt = Instant.now();
        this.accountType = accountType;
        this.name = name;
        this.accountBalance = new Money(BigDecimal.ZERO);
        this.transactionHistory = new ArrayList<>();
        this.dailyLimitAmount = new Money(dailyLimitAmount);
        this.weeklyLimitAmount = new Money(weeklyLimitAmount);
        this.monthlyLimitAmount = new Money(monthlyLimitAmount);

        refreshLimits();

    }

    public void refreshLimits() {
        // Day
        Instant now = Instant.now();

        ZoneId zone = ZoneId.of("Europe/Berlin");
        Instant todayStart = now.atZone(zone).toLocalDate().atStartOfDay(zone).toInstant();
        if (accountType == null || Objects.requireNonNull(updatedAt).isBefore(todayStart)) {
            if (accountType == MomoAccountType.MTN) {
                dailyLimitAmount = new Money(2500000);
            } else {
                dailyLimitAmount = new Money(2000000);
            }
        }


        // Week (ISO week, Monday start)
        LocalDate weekStartDate = now.atZone(zone).toLocalDate().with(java.time.DayOfWeek.MONDAY);
        Instant weekStart = weekStartDate.atStartOfDay(zone).toInstant();
        if (accountType == null || updatedAt.isBefore(weekStart)) {
            if (accountType == MomoAccountType.MTN) {
                dailyLimitAmount = new Money(10000000);
            } else {
                dailyLimitAmount = new Money(5000000);
            }
        }

        // Month
        LocalDate monthStartDate = now.atZone(zone).toLocalDate().withDayOfMonth(1);
        Instant monthStart = monthStartDate.atStartOfDay(zone).toInstant();
        if (accountType == null || updatedAt.isBefore(monthStart)) {
            if (accountType == MomoAccountType.MTN) {
                dailyLimitAmount = new Money(20000000);
            } else {
                dailyLimitAmount = new Money(10000000);
            }
        }
        updatedAt = now;
    }

    public void addTransaction(Transaction transaction) {
        Objects.requireNonNull(transaction, "transaction");
        transaction.setOwnerId(this.id);
        this.transactionHistory.add(transaction);
    }

    public Transaction deposit(Money money, Instant createdAt, String description) {
        Objects.requireNonNull(money);
        Objects.requireNonNull(createdAt);
        this.accountBalance = this.accountBalance.add(money);
        Transaction done = new Transaction(money, this.accountBalance, createdAt, TransactionType.DEPOSIT, description, null);
        addTransaction(done);
        return done;
    }

    public Transaction withdraw(Money money, Instant createdAt, String description) {
        if (!this.accountBalance.isGreaterOrEqual(money)) {
            throw new InsufficientFundsException("you cannot make withdrawal from this Momo account of " + money.getValue() + " because account balane is " + this.accountBalance.getValue(), Map.of("momoId", this.id));
        }
        refreshLimits();
        if (money.isGreaterThan(dailyLimitAmount)) {
            throw new AccountLimitException(Code.DAILY_LIMIT_ERROR, "Daily limit reached, you can only deposit: " + dailyLimitAmount + " or less: mobile money account 107", Map.of("momoId", id));
        }
        if (money.isGreaterThan(weeklyLimitAmount)) {
            throw new AccountLimitException(Code.WEEKLY_LIMIT_ERROR, "Weekly limit reached, you can only deposit: " + dailyLimitAmount + " or less: mobile money account 107", Map.of("momoId", id));
        }
        if (money.isGreaterThan(monthlyLimitAmount)) {
            throw new AccountLimitException(Code.MONTHLY_LIMIT_ERROR, "Monthly limit reached, you can only deposit: " + dailyLimitAmount + " or less: mobile money account 107", Map.of("momoId", id));
        }
        this.accountBalance = this.accountBalance.subtract(money);

        Transaction doneTransaction = new Transaction(money, new Money(accountBalance.getValue()), createdAt, TransactionType.WITHDRAWAL, description, null);
        dailyLimitAmount=dailyLimitAmount.subtract(money);
        weeklyLimitAmount=weeklyLimitAmount.subtract(money);
        monthlyLimitAmount=monthlyLimitAmount.subtract(money);
        addTransaction(doneTransaction);
        return doneTransaction;
    }


    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public MomoAccountType getAccountType() {
        return accountType;
    }

    public void setAccountBalance(Money accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Money getAccountBalance() {
        return accountBalance;
    }

    public String getName() {
        return name;
    }

    @Override
    public Long getAccountId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Money getWeeklyLimitAmount() {
        return weeklyLimitAmount;
    }

    public void setWeeklyLimitAmount(Money weeklyLimitAmount) {
        this.weeklyLimitAmount = weeklyLimitAmount;
    }

    public Money getMonthlyLimitAmount() {
        return monthlyLimitAmount;
    }

    public void setMonthlyLimitAmount(Money monthlyLimitAmount) {
        this.monthlyLimitAmount = monthlyLimitAmount;
    }

    public Money getDailyLimitAmount() {
        return dailyLimitAmount;
    }

    public void setDailyLimitAmount(Money dailyLimitAmount) {
        this.dailyLimitAmount = dailyLimitAmount;
    }
}
