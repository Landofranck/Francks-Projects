package project.adapter.out.persistence.EntityModels.BettingAccount;


import jakarta.persistence.*;
import project.domain.model.Enums.BrokerType;

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
    private DraftSlipEntity draftBetSlip;

    @Column(name = "broker_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BrokerType brokerType;
    private BigDecimal balance;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BettingAccountTransactionEntity> transactionHistory = new ArrayList<>();

    @OneToMany(mappedBy = "parentAccountEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BetSlipEntity> betHistory = new ArrayList<>();

    public BettingAccountEntity() {
    }

    public void setDraftBetSlip(DraftSlipEntity draftBetSlip) {
        this.draftBetSlip = draftBetSlip;
    }

    public void putNewBetSlip(DraftSlipEntity incoming) {
        if (incoming == null) {
            return;
        }

        // 1) Ensure draft exists
        if (this.draftBetSlip == null) {
            this.draftBetSlip = new DraftSlipEntity();
            // IMPORTANT: set owning side of OneToOne
            this.draftBetSlip.setDraftBetSlipOwner(this); // sets draftBetSlipOwner
        }

        // 2) Copy data into the managed draft
        this.draftBetSlip.setStatus(incoming.getStatus());
        this.draftBetSlip.setCategory(incoming.getCategory());
        this.draftBetSlip.setTotalOdd(incoming.getTotalOdd());
        this.draftBetSlip.setStake(incoming.getStake());

        // picks: attach properly (so parent pointers are correct)
        this.draftBetSlip.getPicks().clear();
        if (incoming.getPicks() != null) {
            for (DraftEventPickEntity p : incoming.getPicks()) {
                this.draftBetSlip.addDraftEventPick(p);
            }
        }

        // also set parentAccountEntity if you want the draft tied to account logically
        this.draftBetSlip.setDraftBetSlipOwner(this);
    }

    public void addBetSlipEntity(BetSlipEntity betSlip) {
        this.betHistory.add(betSlip);
        betSlip.setParentAccountEntity(this);
    }

    public DraftSlipEntity getDraftBetSlip() {
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

    public BrokerType getBrokerType() {
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


    public void setBrokerType(BrokerType brokerType) {
        this.brokerType = brokerType;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

}
