package project.adapter.out.persistence.EntityModels;

import project.domain.model.Enums.AccountType;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "betting_account",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"account_name", "broker_type"}
        )
)
public class BettingAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // generated
    @Column(name = "account_name", nullable = false)
    private String accountName;

    @OneToOne(mappedBy = "draftBetSlipOwner", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private BetSlipEntity draftBetSlip;

    @Column(name = "broker_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType brokerType;
    private BigDecimal balance;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BettingAccountTransactionEntity> transactionHistory = new ArrayList<>();

    @OneToMany(mappedBy = "parentAccountEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BetSlipEntity> betHistory = new ArrayList<>();

    protected BettingAccountEntity() {
    }

    public void putNewBetSlip(BetSlipEntity betSlip) {

        this.draftBetSlip.setStatus(betSlip.getStatus());
        this.draftBetSlip.setPicks(betSlip.getPicks());
        this.draftBetSlip.setCategory(betSlip.getCategory());
        this.draftBetSlip.setTotalOdd(betSlip.getTotalOdd());
        betSlip.setParentAccount(this);
    }

    public void addBetSlipEntity(BetSlipEntity betSlip) {
        this.betHistory.add(betSlip);
        betSlip.setParentAccount(this);
    }

    public BetSlipEntity getDraftBetSlip() {
        return draftBetSlip;
    }

    public void addTransactionEntity(BettingAccountTransactionEntity transaction) {
        this.transactionHistory.add(transaction);
        transaction.setOwner(this);
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<BetSlipEntity> getBetHistory() {
        return betHistory;
    }

    public void setBetHistory(List<BetSlipEntity> betHistory) {
        this.betHistory = betHistory;
    }

    public List<BettingAccountTransactionEntity> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(List<BettingAccountTransactionEntity> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }


    public void setBrokerType(AccountType brokerType) {
        this.brokerType = brokerType;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

}
