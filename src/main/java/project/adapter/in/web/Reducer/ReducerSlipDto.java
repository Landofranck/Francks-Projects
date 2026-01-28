package project.adapter.in.web.Reducer;

import project.adapter.in.web.MatchEventPickDto;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BrokerType;

import java.math.BigDecimal;
import java.util.List;
public record ReducerSlipDto(
 List<MatchEventPickDto> picks,
 BetCategory category,
 BrokerType brokerType,
 BigDecimal planedStake,
 BigDecimal RemainingStake,
 double totalOdds,
 int numberOfEvents
){}