package project.adapter.in.web.Reducer;

import jakarta.validation.constraints.NotNull;
import project.adapter.in.web.MatchEventPickDto;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BrokerType;

import java.math.BigDecimal;
import java.util.List;

public record ReducerSlipDto(
        @NotNull
        BetCategory category,
        BrokerType brokerType,
        BigDecimal planedStake,
        BigDecimal RemainingStake,
        double totalOdds,
        int numberOfEvents,
        BigDecimal potentialWinings,
        List<MatchEventPickDto> picks
) {
}