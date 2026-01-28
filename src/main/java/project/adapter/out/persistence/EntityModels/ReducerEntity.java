package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;
import project.adapter.out.persistence.Embeddables.BlockEmb;
import project.adapter.out.persistence.EntityModels.BettingAccount.BetSlipEntity;
import project.adapter.out.persistence.EntityModels.BettingAccount.MatchEntity;
import project.adapter.out.persistence.EntityModels.BettingAccount.MatchEventPickEntity;
import project.domain.model.BetSlip;
import project.domain.model.Enums.BetCategory;
import project.domain.model.MatchEventPick;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class ReducerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal totalStake;
    @ManyToMany
    @JoinTable(name = "reducer_matches",
            joinColumns = @JoinColumn(name = "reducer_id"),
            inverseJoinColumns = @JoinColumn(name = "match_id"))
    private List<MatchEntity> betMatchEntities = new ArrayList<>();
    @OneToMany(mappedBy ="reducerParent",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ReducerBetSlipEntity> slips = new ArrayList<>();
    private BigDecimal bonusAmount;
    @ElementCollection
    private List<BlockEmb> blockEmbs=new ArrayList<>();

    public ReducerEntity() {
    }

    public void addMatches(MatchEntity match) {
        Objects.requireNonNull(match, "match");
        betMatchEntities.add(match);
        match.addParent(this);
    }
    public void deleteMatch(MatchEntity match) {
        Objects.requireNonNull(match, "match");
        betMatchEntities.remove(match);
        match.removeParent(this);
    }

    public void addEventToSlip(BetSlip slip, MatchEventPick pick) {
        slip.addMatchEventPick(pick);
        slip.makeTotalOdds();
    }


    public void addBetSlipEmbd(ReducerBetSlipEntity b) {
        Objects.requireNonNull(b, "betSlip");
        slips.add(b);
        b.setReducerParent(this);
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


    public List<MatchEntity> getBetMatcheEntities() {
        return betMatchEntities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalStake() {
        return totalStake;
    }

    public void setTotalStake(BigDecimal totalStake) {
        this.totalStake = totalStake;
    }

    public List<MatchEntity> getBetMatchEntities() {
        return betMatchEntities;
    }

    public void setBetMatchEntities(List<MatchEntity> betMatchEntities) {
        this.betMatchEntities = betMatchEntities;
    }

    public List<ReducerBetSlipEntity> getSlips() {
        return slips;
    }

    public void setSlips(List<ReducerBetSlipEntity> slips) {
        this.slips = slips;
    }

    public BigDecimal getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(BigDecimal bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public List<BlockEmb> getBlockEmbs() {
        return blockEmbs;
    }

    public void setBlockEmbs(List<BlockEmb> blockEmbs) {
        this.blockEmbs = blockEmbs;
    }
}
