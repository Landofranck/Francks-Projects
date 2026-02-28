package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;
import project.adapter.in.web.Utils.Code;
import project.adapter.out.persistence.Embeddables.BlockEmb;
import project.adapter.out.persistence.EntityModels.BettingAccount.BetSlipEntity;
import project.adapter.out.persistence.EntityModels.BettingAccount.BettingAccountEntity;
import project.adapter.out.persistence.EntityModels.BettingAccount.MatchEntity;
import project.adapter.out.persistence.EntityModels.BettingAccount.SlipEventPickEntity;
import project.application.error.ValidationException;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BetStrategy;
import project.domain.model.Enums.BrokerType;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class ReducerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ElementCollection
    private List<ShuffleEmb> shuffleCombinations=new ArrayList<>(){};
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
    private Long matchVersion=0L;
    private BetStrategy strategy;
    @Enumerated
    private BrokerType broker;
    private BigDecimal profitOrLoss;
    private BigDecimal totalStaked;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentReducerSummaryEntity_id",nullable = true)
    private ReducerSummaryEntity parentReducerSummaryEntity;

    public ReducerEntity() {
    }

    public void addMatches(MatchEntity match) {
        if(match.getBroker()!=this.broker)
            throw new ValidationException(Code.MATCH_ERROR,"You can only add matches of the broker: "+ this.broker+", to the reducer",Map.of("reducerId",id));
        if(match.getReducers().contains(this)||this.betMatchEntities.contains(match))
            throw new ValidationException(Code.MATCH_ALREADY_EXISTS,"This match is already found in the reducer with id "+id+" recducer entity 45",Map.of("reducerId",id));
        Objects.requireNonNull(match, "match");
        betMatchEntities.add(match);
        match.addParent(this);
        slips.clear();
    }
    public void deleteMatch(MatchEntity match) {
        Objects.requireNonNull(match, "match");
        betMatchEntities.remove(match);
        match.removeParent(this);
        slips.clear();
    }

    public void addBetSlipEntity(ReducerBetSlipEntity b) {
        Objects.requireNonNull(b, "betSlip");
        slips.add(b);
        b.setReducerParent(this);
    }


    // ---- helpers ----

    private SlipEventPickEntity copyPick(SlipEventPickEntity original) {
        Objects.requireNonNull(original, "original pick");
        SlipEventPickEntity copy = new SlipEventPickEntity();
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
        for (SlipEventPickEntity p : original.getPicks()) {
            copy.addSlipEventPickEntity(copyPick(p));
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

    public void setMatchVersion(Long matchVersion) {
        this.matchVersion = matchVersion;
    }

    public Long getMatchVersion() {
        return matchVersion;
    }

    public BetStrategy getStrategy() {
        return this.strategy;
    }

    public void setStrategy(BetStrategy strategy) {
        this.strategy = strategy;
    }

    public BrokerType getBroker() {
        return broker;
    }

    public void setBroker(BrokerType broker) {
        this.broker = broker;
    }

    public BigDecimal getProfitOrLoss() {
        return this.profitOrLoss;
    }
    public void setProfitOrLoss(BigDecimal profitOrLoss) {
        this.profitOrLoss = profitOrLoss;
    }

    public void setTotalStaked(BigDecimal totalStaked) {
        this.totalStaked = totalStaked;
    }

    public BigDecimal getTotalStaked() {
        return totalStaked;
    }

    public void setParentReducerSummaryEntity(ReducerSummaryEntity parentReducerSummaryEntity) {
        this.parentReducerSummaryEntity = parentReducerSummaryEntity;
    }

    public ReducerSummaryEntity getParentReducerSummaryEntity() {
        return parentReducerSummaryEntity;
    }

    public List<ShuffleEmb> getShuffleCombinations() {
        return shuffleCombinations;
    }

    public void setShuffleCombinations(List<ShuffleEmb> shuffleCombinations) {
        this.shuffleCombinations = shuffleCombinations;
    }
}
