package project.domain.model;

import project.adapter.in.web.Utils.Code;
import project.application.error.InsufficientFundsException;
import project.application.error.ResourceNotFoundException;
import project.application.error.ValidationException;
import project.domain.model.Enums.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        this.bonuses = new ArrayList<>();
        this.draftBetSlip = new DraftBetSlip(BetCategory.DRAFT);
    }

    public void addBetSlip(BetSlip newBetslip) {
        newBetslip.setParentAccountId(this.id);
        this.betHistory.add(newBetslip);
    }

    public void addBonus(Bonus b) {
        b.updateStatus();
        this.bonuses.add(b);
    }

    public Transaction deposit(Money money, Instant createdAt, String description) {
        this.balance = this.balance.add(money);
        if (money.isGreaterThan(500000)) {
            throw new ValidationException(Code.INVALID_AMOUNT, "you cannot deposit an amount greater than 500000: betting account 82", Map.of("momoId", id));
        }
        Transaction doneTransaction = new Transaction(
                money,
                new Money(balance.getValue()),
                createdAt,
                TransactionType.DEPOSIT, description, null
        );

        addTransaction(doneTransaction);
        return doneTransaction;
    }


    public void addTransaction(Transaction transaction) {
        transaction.setOwnerId(this.id);
        this.transactionHistory.add(transaction);
    }

    public void putEmptySlip(DraftBetSlip betSlip) {
        if (betSlip == null)
            throw new ValidationException(Code.BET_SLIP_NOT_FOUND, "there must be a bet slip line 56 betting account: betting account 70", Map.of("bettingId", id));
        betSlip.setDraftSlipOwner(this);
        this.draftBetSlip = betSlip;
    }

    public DraftBetSlip getDraftBetSlip() {
        return draftBetSlip;

    }

    public Transaction placeBetTransaction(Money money, String description, Long betSlipId) {
        if (!this.balance.isGreaterThan(money)) {
            throw new ValidationException(Code.INVALID_AMOUNT, "Your account balance is not sufficient to place that bet : betting account 82 " + money.getValue(), Map.of("bettingId", id));
        }
        if (this.brokerType == BrokerType.ONE_X_BET && !money.isGreaterThan(90)) {
            throw new ValidationException(Code.INVALID_AMOUNT, "Your minimum betting amount is 90 : betting account 85 " + money.getValue(), Map.of("bettingId", id));
        }
        if (this.brokerType == BrokerType.BET_MOMO && !money.isGreaterThan(100)) {
            throw new ValidationException(Code.INVALID_AMOUNT, "Your minimum betting amount is 90 : betting account 88" + money.getValue(), Map.of("bettingId", id));
        }
        this.balance = balance.subtract(money);
        return new Transaction(money, new Money(balance.getValue()), Instant.now(), TransactionType.BET_PLACED, description, betSlipId);
    }

    public Transaction betWonTransaction(Money money, Instant now,String description,  Long betSlipId) {
        this.balance = balance.add(money);
        return new Transaction(money, new Money(balance.getValue()), now, TransactionType.BET_WON, description, betSlipId);
    }

    public Transaction betRefundedTransaction(Money stake, String description, Long betSlipId) {
        this.balance = balance.add(stake);
        return new Transaction(stake, new Money(balance.getValue()), Instant.now(), TransactionType.BET_REFUNDED, description, betSlipId);

    }

    public void placeBet(Money stake, Instant now) {
        this.draftBetSlip.setStake(stake);
        this.draftBetSlip.setCreatedAt(now);
        this.draftBetSlip.setStatus(BetStatus.PENDING);
        this.draftBetSlip.setBonusSlip(false);
    }

    public void placeBonusBet(Integer bonusIndex, Instant now) {
        try {
            var b = this.bonuses.get(bonusIndex);

            if (b.getStatus().equals(BonusStatus.EXPIRED) || b.getStatus().equals(BonusStatus.REDEEMED))
                throw new ValidationException(Code.INVALID_BONUS_INDEX, "This bonus has expired or has been redeemed; bettingAccount 117", Map.of("bettingId", id));
            b.setStatus(BonusStatus.REDEEMED);
            this.draftBetSlip.setStatus(BetStatus.PENDING);
            this.draftBetSlip.setStake(b.getAmount());
            this.draftBetSlip.setBonusSlip(true);
            this.draftBetSlip.calculatePotentialWinning();
            this.draftBetSlip.setCreatedAt(now);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ResourceNotFoundException(Code.INVALID_BONUS_INDEX, "that bonus does not exist in this account: betting account 125", Map.of("bettingId", id));
        }
    }

    public Transaction withdraw(Money money, String description) {
        if (money.isGreaterThan(500000)) {
            throw new ValidationException(Code.INVALID_AMOUNT, "you cannot withdraw an amount greater than 500000: betting account 131", Map.of("momoId", id));
        }
        if (!this.balance.isGreaterThan(money)) {
            throw new InsufficientFundsException("you cannot make withdrawal of: betting account 134 " + money.getValue(), Map.of("bettingId", this.id));
        }
        this.balance = balance.subtract(money);
        return new Transaction(money, new Money(balance.getValue()), Instant.now(), TransactionType.WITHDRAWAL, description, null);
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
