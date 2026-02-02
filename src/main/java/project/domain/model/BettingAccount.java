package project.domain.model;

import project.domain.model.Enums.BrokerType;
import project.domain.model.Enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BettingAccount implements Account {
    private Long id; // generated
    private final String accountName;
    private final BrokerType brokerType;
    private Money balance;
    private List<Transaction> transactionHistory;
    private List<BetSlip> betHistory;
    private DraftBetSlip draftBetSlip;
    private List<Bonus> Bonuses;

    public BettingAccount(String accountName, BrokerType brokerType) {
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


    public void addTransaction(Transaction transaction) {
        transaction.setOwner(this);
        this.transactionHistory.add(transaction);
    }

    public DraftBetSlip putEmptySlip(DraftBetSlip betSlip) {
        if(betSlip==null)throw new RuntimeException("there must be a betslip line 56 betting account");
        betSlip.setDraftSlipOwner(this);
        this.draftBetSlip =betSlip;
        return betSlip;
    }

    public DraftBetSlip getDraftBetSlip() {
            return draftBetSlip;

    }
    public Transaction placeBet(Money money, Instant now, String description) {
        if (!this.balance.isGreaterThan(money)) {
            throw new IllegalArgumentException("Your account balance is not sufficient to place that bet " + money.getValue());
        }
        this.balance = balance.subtract(money);
        Transaction doneTransaction = new Transaction(money, new Money(balance.getValue()), Instant.now(), TransactionType.BET_PLACED, description);
        return doneTransaction;
    }

    public Transaction withdraw(Money money, Instant now, String description) {
        if (!this.balance.isGreaterThan(money)) {
            throw new IllegalArgumentException("you cannot make withdrwal of " + money.getValue());
        }
        this.balance = balance.subtract(money);
        Transaction doneTransaction = new Transaction(money, new Money(balance.getValue()), Instant.now(), TransactionType.WITHDRAWAL, description);
        return doneTransaction;
    }

    public String getAccountName() {
        return accountName;
    }

    public BrokerType getBrokerType() {
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

    public BrokerType getAccountType() {
        return this.brokerType;
    }
}
