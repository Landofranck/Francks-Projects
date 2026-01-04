package project.domain.model;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public MobileMoneyAccount(Long id, AccountType accountType) {
        this.accountType = accountType;
        this.accountBalance = new Money(BigDecimal.ZERO);
        this.dailyLimit = false;
        this.weeklyLimit = false;
        this.monthlyLimit = false;
        transactionHistory = new ArrayList<>();
        this.id=id;
    }

    public void addTransaction(Transaction transaction) {
        Objects.requireNonNull(transaction, "transaction");
        transaction.setOwner(this);
        this.transactionHistory.add(transaction);
    }

    public Transaction deposit(Money money, Instant createdAt, String description) {
        Objects.requireNonNull(money);
        Objects.requireNonNull(createdAt);
        this.accountBalance = this.accountBalance.add(money);
        Transaction done = new Transaction(money, this.accountBalance, createdAt, TransactionType.DEPOSIT, description);
        addTransaction(done);
        return done;
    }

    public Transaction withdraw(Money money, Instant createdAt,String description) {
        if (!this.accountBalance.isGreaterOrEqual(money)) {
            throw new IllegalArgumentException("you cannot make withdrawal of " + money.getValue());
        }

        this.accountBalance = this.accountBalance.subtract(money);

        Transaction doneTransaction = new Transaction(
                money,
                new Money(accountBalance.getValue()),
                createdAt,
                TransactionType.WITHDRAWAL,
                description
        );

        addTransaction(doneTransaction);
        return doneTransaction;
    }

    /**
     * Recomputes daily/weekly/monthly withdrawal limits based on transactionHistory.
     */
    public void updateLimits(Instant now, ZoneId zoneId) {
        Objects.requireNonNull(now, "now");
        Objects.requireNonNull(zoneId, "zoneId");

        BigDecimal dailyThreshold;
        BigDecimal weeklyThreshold;
        BigDecimal monthlyThreshold;

        // Set thresholds by account type
        switch (accountType) {
            case MTN -> {
                dailyThreshold = BigDecimal.valueOf(2_500_000);
                weeklyThreshold = BigDecimal.valueOf(10_000_000);
                monthlyThreshold = BigDecimal.valueOf(20_000_000);
            }
            case ORANGE -> {
                dailyThreshold = BigDecimal.valueOf(2_000_000);
                weeklyThreshold = BigDecimal.valueOf(5_000_000);
                monthlyThreshold = BigDecimal.valueOf(10_000_000);
            }
            default -> throw new IllegalStateException("Unsupported account type: " + accountType);
        }// Period starts
        Instant dayStart = startOfDay(now, zoneId);
        Instant weekStart = startOfWeekMonday(now, zoneId);
        Instant monthStart = startOfMonth(now, zoneId);

        BigDecimal dailyWithdrawals = sumWithdrawalsSince(dayStart);
        BigDecimal weeklyWithdrawals = sumWithdrawalsSince(weekStart);
        BigDecimal monthlyWithdrawals = sumWithdrawalsSince(monthStart);

        this.dailyLimit = dailyWithdrawals.compareTo(dailyThreshold) >= 0;
        this.weeklyLimit = weeklyWithdrawals.compareTo(weeklyThreshold) >= 0;
        this.monthlyLimit = monthlyWithdrawals.compareTo(monthlyThreshold) >= 0;
    }

    private BigDecimal sumWithdrawalsSince(Instant startInclusive) {
        BigDecimal total = BigDecimal.ZERO;

        for (Transaction t : transactionHistory) {
            if (t.getType() == TransactionType.WITHDRAWAL
                    && !t.getCreatedAt().isBefore(startInclusive)) {
                total = total.add(t.getTransactionAmmount().getValue());
            }
        }

        return total;
    }

    private static Instant startOfDay(Instant now, ZoneId zoneId) {
        LocalDate date = now.atZone(zoneId).toLocalDate();
        return date.atStartOfDay(zoneId).toInstant();
    }

    private static Instant startOfWeekMonday(Instant now, ZoneId zoneId) {
        LocalDate date = now.atZone(zoneId).toLocalDate();
        LocalDate monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return monday.atStartOfDay(zoneId).toInstant();
    }

    private static Instant startOfMonth(Instant now, ZoneId zoneId) {
        LocalDate date = now.atZone(zoneId).toLocalDate();
        LocalDate firstDay = date.withDayOfMonth(1);
        return firstDay.atStartOfDay(zoneId).toInstant();
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
