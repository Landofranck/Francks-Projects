package project.adapter.in.web.Reducer.ReducerDto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateReducerStakeDto(@NotNull BigDecimal stake, BigDecimal bonusStake) {
}
