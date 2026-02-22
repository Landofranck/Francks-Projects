package project.adapter.in.web.Reducer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import project.adapter.in.web.bettinAccountDTO.MatchEventPickDto;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BetStrategy;
import project.domain.model.Enums.BrokerType;

import java.math.BigDecimal;
import java.util.List;

public record ReducerSlipDto(
        @NotNull
        BetCategory category,
        @NotNull
        BrokerType brokerType,
        @PositiveOrZero
        BigDecimal planedStake,
        @PositiveOrZero
        BigDecimal RemainingStake,
        double totalOdds,
        @NotNull
        BetStrategy betStrategy,
        int numberOfEvents,
        BigDecimal potentialWinings,
        double bonusOdds,
        List<MatchEventPickDto> picks
) {
}