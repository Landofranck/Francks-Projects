package project.domain.model;

import project.domain.model.Enums.AccountType;
import project.domain.model.Enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BettingAccount implements Account {
    private  Long id; // generated
    private final String accountName;
    private final AccountType brokerType;
    private Money balance;
    private List<Transaction> transactionHistory;
    private List<BetSlip> betHistory;

    public BettingAccount(String accountName, AccountType brokerType) {
        this.accountName = accountName;
        this.brokerType = brokerType;
        this.balance = new Money(BigDecimal.ZERO);
        this.transactionHistory = new ArrayList<>();
        this.betHistory = new ArrayList<>();
        this.balance=new Money(BigDecimal.ZERO);
    }

    public void addBetSlip(BetSlip newBetslip) {
        newBetslip.setParentAccount(this);
        this.betHistory.add(newBetslip);
        //advicable to use the
        addTransaction(new Transaction(newBetslip.getStake(), new Money(balance.getValue()), newBetslip.getCreatedAt(), TransactionType.BET_PLACED));
    }

    public void addTransaction(Transaction transaction) {
        transaction.setOwner(this);
        this.transactionHistory.add(transaction);
    }

    public void deposit(Money money) {
        this.balance.add(money);
        addTransaction(new Transaction(money, new Money(balance.getValue()), Instant.now(), TransactionType.DEPOSIT));

    }

    public void withdraw(Money money) {
        if (!this.balance.isGreaterThan(money)) {
            throw new RuntimeException("you cannot make withdrwal of " + money.getValue());
        }
        balance.subtract(money);
        addTransaction(new Transaction(money, new Money(balance.getValue()), Instant.now(), TransactionType.WITHDRAWAL));
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

}
