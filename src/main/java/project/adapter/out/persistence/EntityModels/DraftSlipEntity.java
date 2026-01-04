package project.adapter.out.persistence.EntityModels;


import jakarta.persistence.*;
import project.domain.model.Enums.BetStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DraftSlipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "parentBetSlipEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DraftEventPickEntity> picks = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private BetStatus status;
    private String category;
    private Instant createdAt;
    private double totalOdd;
    private BigDecimal stake;
    @OneToOne(optional = true,fetch = FetchType.LAZY)
    @JoinColumn(name = "betting_account_id",unique = true)
    private BettingAccountEntity draftBetSlipOwner;
private BigDecimal potentialWinning;

    public BettingAccountEntity getDraftBetSlipOwner() {
        return draftBetSlipOwner;
    }

    public void setDraftBetSlipOwner(BettingAccountEntity newBetslipParent) {
        this.draftBetSlipOwner = newBetslipParent;
    }

    protected DraftSlipEntity() {
    }

    public void setPotentialWinning(BigDecimal potentialWinning) {
        this.potentialWinning = potentialWinning;
    }

    public BigDecimal getPotentialWinning() {
        return potentialWinning;
    }

    public void setTotalOdd(double totalOdd) {
        this.totalOdd = totalOdd;
    }

    public double getTotalOdd() {
        return totalOdd;
    }

    public void addDraftEventPick(DraftEventPickEntity entity) {
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

    public List<DraftEventPickEntity> getPicks() {
        return picks;
    }

    public String getCategory() {
        return category;
    }

    public void setPicks(List<DraftEventPickEntity> picks) {
        this.picks = picks;
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


    public void setStake(BigDecimal stake) {
        this.stake = stake;
    }

}
