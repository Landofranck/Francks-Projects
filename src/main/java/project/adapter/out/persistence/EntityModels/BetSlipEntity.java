package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;
import project.domain.model.Enums.BetStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BetSlipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "parentBetSlipEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchEventPickEntity> picks = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private BetStatus status;
    private String category;
    private Instant createdAt;
    private double totalOdd;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentAccountEntity_id", nullable = false)
    private BettingAccountEntity parentAccountEntity;


    @OneToOne(optional = true,fetch = FetchType.LAZY)
    @JoinColumn(name = "betting_account_id",unique = true)
    private BettingAccountEntity draftBetSlipOwner;
    private BigDecimal stake;

    public BettingAccountEntity getNewBetslipParent() {
        return draftBetSlipOwner;
    }

    public void setNewBetslipParent(BettingAccountEntity newBetslipParent) {
        this.draftBetSlipOwner = newBetslipParent;
    }

    protected BetSlipEntity() {
    }



    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public void setTotalOdd(double totalOdd) {
        this.totalOdd = totalOdd;
    }

    public double getTotalOdd() {
        return totalOdd;
    }

    public void addMatchEventPickEntity(MatchEventPickEntity entity) {
        this.picks.add(entity);
        entity.setParent(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BetStatus getStatus() {
        return status;
    }

    public void setStatus(BetStatus status) {
        this.status = status;
    }

    public List<MatchEventPickEntity> getPicks() {
        return picks;
    }

    public String getCategory() {
        return category;
    }

    public void setPicks(List<MatchEventPickEntity> picks) {
        this.picks = picks;
    }


    public BettingAccountEntity getParentAccount() {
        return parentAccountEntity;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getStake() {
        return stake;
    }


    public void setCategory(String category) {
        this.category = category;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setParentAccount(BettingAccountEntity parentAccount) {
        this.parentAccountEntity = parentAccount;
    }

    public void setStake(BigDecimal stake) {
        this.stake = stake;
    }

}
