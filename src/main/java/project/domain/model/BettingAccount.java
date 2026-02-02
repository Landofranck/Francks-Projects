package project.domain.model;

import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.BonusStatus;
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
    private final List<Transaction> transactionHistory;
    private final List<BetSlip> betHistory;
    private DraftBetSlip draftBetSlip;
    private final List<Bonus> bonuses;

    public BettingAccount(String accountName, BrokerType brokerType) {
        this.accountName = accountName;
        this.brokerType = brokerType;
        this.balance = new Money(BigDecimal.ZERO);
        this.transactionHistory = new ArrayList<>();
        this.betHistory = new ArrayList<>();
        this.balance = new Money(BigDecimal.ZERO);
        this.bonuses=new ArrayList<>();
    }

    public void addBetSlip(BetSlip newBetslip) {
        newBetslip.setParentAccount(this);
        this.betHistory.add(newBetslip);
    }

    public void addBonus(Bonus b) {
        b.updateStatus();
        this.bonuses.add(b);
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
        if (betSlip == null) throw new RuntimeException("there must be a bet slip line 56 betting account");
        betSlip.setDraftSlipOwner(this);
        this.draftBetSlip = betSlip;
        return betSlip;
    }

    public DraftBetSlip getDraftBetSlip() {
        return draftBetSlip;

    }

    public Transaction placeBetTransaction(Money money, String description) {
        if (!this.balance.isGreaterThan(money)) {
            throw new IllegalArgumentException("Your account balance is not sufficient to place that bet " + money.getValue());
        }
        this.balance = balance.subtract(money);
        return new Transaction(money, new Money(balance.getValue()), Instant.now(), TransactionType.BET_PLACED, description);
    }

    public void placeBet(Money stake, Instant now) {
        this.draftBetSlip.setStake(stake);
        this.draftBetSlip.setCreatedAt(now);
        this.draftBetSlip.setStatus(BetStatus.PENDING);
        this.draftBetSlip.setBonusSlip(false);
    }

    public void placeBonusBet(Integer bonusIndex, Instant now) {
        try {
            var b=this.bonuses.get(bonusIndex);

        if(b.getStatus().equals(BonusStatus.EXPIRED))
            throw new IllegalArgumentException("This bonus has expired; bettingAccount 83");
        b.setStatus(BonusStatus.REDEEMED);
        this.draftBetSlip.setStatus(BetStatus.PENDING);
        this.draftBetSlip.setStake(b.getAmount());
        this.draftBetSlip.calculatPotentialWinning();
        this.draftBetSlip.setCreatedAt(now);
        this.draftBetSlip.setBonusSlip(true);
        }catch (ArrayIndexOutOfBoundsException e){
            throw new IllegalArgumentException("that bonus does not exist in this account: betting account 100");
        }
    }

    public Transaction withdraw(Money money, String description) {
        if (!this.balance.isGreaterThan(money)) {
            throw new IllegalArgumentException("you cannot make withdrawal of " + money.getValue());
        }
        this.balance = balance.subtract(money);
        return new Transaction(money, new Money(balance.getValue()), Instant.now(), TransactionType.WITHDRAWAL, description);
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



    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
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

    public List<Bonus> getBonuses() {
        return bonuses;
    }

}
