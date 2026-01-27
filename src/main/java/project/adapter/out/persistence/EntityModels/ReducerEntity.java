package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;
import project.domain.model.BetSlip;
import project.domain.model.Enums.BetCategory;
import project.domain.model.MatchEventPick;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class ReducerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal totalStake;
    @OneToMany(mappedBy = "matchReducerParent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MatchEntity> betMatchEntities = new ArrayList<>();
    @OneToMany(mappedBy = "betSlipReducerParent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BetSlipEntity> slips = new ArrayList<>();
    private BigDecimal bonusAmount;

    public ReducerEntity() {
    }

    public void addMatches(MatchEntity match) {
        Objects.requireNonNull(match, "match");
        betMatchEntities.add(match);
        match.setMatchReducerParent(this);
    }

    public void addEventToSlip(BetSlip slip, MatchEventPick pick) {
        slip.addMatchEventPick(pick);
        slip.makeTotalOdds();
    }


    public void addBetSlipEntity(BetSlipEntity b) {
        Objects.requireNonNull(b, "betSlip");
        slips.add(b);
        b.setBetSlipReducerParent(this);
    }


    // ---- helpers ----

    private MatchEventPickEntity copyPick(MatchEventPickEntity original) {
        Objects.requireNonNull(original, "original pick");
        MatchEventPickEntity copy = new MatchEventPickEntity();
        copy.setMatchKey(original.getMatchKey());
        copy.setOutcomeName(original.getOutcomeName());
        copy.setOdd(original.getOdd());
        return copy;
    }

    /**
     * Copies a slip's picks into a new slip instance (deep copy of picks).
     */
    private BetSlipEntity copySlip(BetSlipEntity original, BetCategory category) {
        BetSlipEntity copy = new BetSlipEntity();
        copy.setCategory(category);
        for (MatchEventPickEntity p : original.getPicks()) {
            copy.addMatchEventPickEntity(copyPick(p));
        }

        // copy.setCreatedAt(original.getCreatedAt()); // if you want
        // copy.setStake(original.getStake());         // if you want

        return copy;
    }


    public List<BetSlipEntity> getSlips() {
        return this.slips;
    }

    public List<MatchEntity> getBetMatches() {
        return betMatchEntities;
    }

    public BigDecimal getTotalStake() {
        return totalStake;
    }

    public BigDecimal getBonusAmount() {
        return bonusAmount;
    }

    public void setBetMatches(List<MatchEntity> betMatches) {
        this.betMatchEntities = betMatches;
    }

    public void setBonusAmount(BigDecimal bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public void setSlips(List<BetSlipEntity> slips) {
        this.slips = slips;
    }

    public void setTotalStake(BigDecimal totalStake) {
        this.totalStake = totalStake;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
