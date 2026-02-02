package project.adapter.in.web.Reducer;

import jakarta.validation.constraints.NotNull;
import project.domain.model.Enums.BetStrategy;
import project.domain.model.Enums.BrokerType;

import java.math.BigDecimal;

public class CreateReducerDto {

    @NotNull(message = "TotalStake must have a value")
    private BigDecimal totalStake;

    @NotNull(message = "bonus amount must not be null")
    private BigDecimal bonusAmount;
    @NotNull
    private BetStrategy betStrategy;
    @NotNull
    private BrokerType broker;


    public BigDecimal getBonusAmount() {
        return bonusAmount;
    }

    public BigDecimal getTotalStake() {
        return totalStake;
    }

    public BetStrategy getBetStrategy() {
        return betStrategy;
    }

    public BrokerType getBroker() {
        return broker;
    }
}
