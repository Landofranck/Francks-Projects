package project.adapter.in.web.Reducer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.ReadMatchEventPickDto;
import project.adapter.in.web.Utils.Link;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BetStrategy;
import project.domain.model.Enums.BrokerType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReducerSlipDto {
    @NotNull
    private BetCategory category;
    @NotNull
    private BrokerType brokerType;
    @PositiveOrZero
    private BigDecimal planedStake;
    @PositiveOrZero
    private BigDecimal RemainingStake;
    double totalOdds;
    @NotNull
    private BetStrategy betStrategy;
    private int numberOfEvents;
    private BigDecimal potentialWinings;
    private double bonusOdds;
    private List<ReadMatchEventPickDto> picks;
    private List<Link> links;
    public ReducerSlipDto(
            BetCategory category,
            BrokerType brokerType,
            BigDecimal planedStake,
            BigDecimal RemainingStake,
            double totalOdds,
            BetStrategy betStrategy,
            int numberOfEvents,
            BigDecimal potentialWinings,
            double bonusOdds,
            List<ReadMatchEventPickDto> picks) {
        this.category = category;
        this.brokerType = brokerType;
        this.planedStake = planedStake;
        this.RemainingStake= RemainingStake;
        this.totalOdds = totalOdds;
        this.betStrategy = betStrategy;
        this.numberOfEvents = numberOfEvents;
        this.potentialWinings=potentialWinings;
        this.bonusOdds=bonusOdds;
        this.picks = picks;
        this.links=new ArrayList<>();
    }

    public @NotNull BetCategory getCategory() {
        return category;
    }

    public void setCategory(@NotNull BetCategory category) {
        this.category = category;
    }

    public @NotNull BrokerType getBrokerType() {
        return brokerType;
    }

    public void setBrokerType(@NotNull BrokerType brokerType) {
        this.brokerType = brokerType;
    }

    public @PositiveOrZero BigDecimal getPlanedStake() {
        return planedStake;
    }

    public void setPlanedStake(@PositiveOrZero BigDecimal planedStake) {
        this.planedStake = planedStake;
    }

    public @PositiveOrZero BigDecimal getRemainingStake() {
        return RemainingStake;
    }

    public void setRemainingStake(@PositiveOrZero BigDecimal remainingStake) {
        RemainingStake = remainingStake;
    }

    public double getTotalOdds() {
        return totalOdds;
    }

    public void setTotalOdds(double totalOdds) {
        this.totalOdds = totalOdds;
    }

    public @NotNull BetStrategy getBetStrategy() {
        return betStrategy;
    }

    public void setBetStrategy(@NotNull BetStrategy betStrategy) {
        this.betStrategy = betStrategy;
    }

    public int getNumberOfEvents() {
        return numberOfEvents;
    }

    public void setNumberOfEvents(int numberOfEvents) {
        this.numberOfEvents = numberOfEvents;
    }

    public BigDecimal getPotentialWinings() {
        return potentialWinings;
    }

    public void setPotentialWinings(BigDecimal potentialWinings) {
        this.potentialWinings = potentialWinings;
    }

    public double getBonusOdds() {
        return bonusOdds;
    }

    public void setBonusOdds(double bonusOdds) {
        this.bonusOdds = bonusOdds;
    }

    public List<ReadMatchEventPickDto> getPicks() {
        return picks;
    }

    public void setPicks(List<ReadMatchEventPickDto> picks) {
        this.picks = picks;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<Link> getLinks() {
        return links;
    }
}