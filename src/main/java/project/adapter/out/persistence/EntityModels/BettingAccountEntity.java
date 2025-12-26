package project.adapter.out.persistence.EntityModels;

import project.domain.model.Enums.AccountType;
import project.domain.model.Money;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BettingAccountEntity implements AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // generated
    private String accountName;
    private AccountType brokerType;
    @Embedded
    private Money balance;
    @OneToMany(mappedBy = "parentAccountEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity> transactionHistory= new ArrayList<>();

    @OneToMany(mappedBy = "parentAccountEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BetSlipEntity> betHistory=new ArrayList<>();

    protected BettingAccountEntity() {
    }

    public void addBetSlipEntity(BetSlipEntity betSlip) {
        this.betHistory.add(betSlip);
        betSlip.setOwner(this);
    }

    public void addTransactionEntity(TransactionEntity transaction) {
        this.transactionHistory.add(transaction);
    }

    public BettingAccountEntity(Long id, String accountName, AccountType brokerType) {
        this.id = id;
        this.accountName = accountName;
        this.brokerType = brokerType;
        this.balance = new Money(BigDecimal.ZERO);
    }


    public Long getId() {
        return id;
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

    public List<BetSlipEntity> getBetHistory() {
        return betHistory;
    }

    public void setBetHistory(List<BetSlipEntity> betHistory) {
        this.betHistory = betHistory;
    }

    public List<TransactionEntity> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(List<TransactionEntity> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    @Override
    public Long getAccountId() {
        return this.id;
    }

    public void setBrokerType(AccountType brokerType) {
        this.brokerType = brokerType;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

}
