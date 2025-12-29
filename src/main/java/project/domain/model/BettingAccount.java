package project.domain.model;

import project.domain.model.Enums.AccountType;
import project.domain.model.Enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BettingAccount implements Account {
    private Long id; // generated
    private final String accountName;
    private final AccountType brokerType;
    private Money balance;
    private List<Transaction> transactionHistory;
    private List<BetSlip> betHistory;
    private DraftBetSlip newBetslip;

    public BettingAccount(String accountName, AccountType brokerType) {
        this.accountName = accountName;
        this.brokerType = brokerType;
        this.balance = new Money(BigDecimal.ZERO);
        this.transactionHistory = new ArrayList<>();
        this.betHistory = new ArrayList<>();
        this.balance = new Money(BigDecimal.ZERO);
    }

    public void addBetSlip(BetSlip newBetslip) {
        newBetslip.setParentAccount(this);
        this.betHistory.add(newBetslip);
        //advicable to use the
        addTransaction(new Transaction(newBetslip.getStake(), new Money(balance.getValue()), newBetslip.getCreatedAt(), TransactionType.BET_PLACED, ""));
    }

    public void addTransaction(Transaction transaction) {
        transaction.setOwner(this);
        this.transactionHistory.add(transaction);
    }

    public Transaction deposit(Money money, Instant createdAt, String description) {
        this.balance = this.balance.add(money);

        Transaction doneTransaction = new Transaction(
                money,
                new Money(balance.getValue()),
                createdAt,
                TransactionType.DEPOSIT, description
        );

        addTransaction(doneTransaction);
        return doneTransaction;
    }

    public DraftBetSlip putEmptySlip(DraftBetSlip betSlip) {
        if(betSlip==null)throw new RuntimeException("there must be a betslip line 57 betting account");
        betSlip.setParentAccount(this);
        this.newBetslip=betSlip;
        return betSlip;
    }

    public DraftBetSlip getNewBetslip() {
            return newBetslip;

    }

    public Transaction withdraw(Money money, Instant now, String description) {
        if (!this.balance.isGreaterThan(money)) {
            throw new RuntimeException("you cannot make withdrwal of " + money.getValue());
        }
        this.balance = balance.subtract(money);
        Transaction doneTransaction = new Transaction(money, new Money(balance.getValue()), Instant.now(), TransactionType.WITHDRAWAL, description);
        return doneTransaction;
    }

    public String getAccountName() {
        return accountName;
    }

    public AccountType getBrokerType() {
        return brokerType;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public List<BetSlip> getBetHistory() {
        return betHistory;
    }

    public void setBetHistory(List<BetSlip> betHistory) {
        this.betHistory = betHistory;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(List<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    @Override
    public Long getAccountId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public AccountType getAccountType() {
        return this.brokerType;
    }
}
