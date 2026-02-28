package project.adapter.in.web.Reducer.ReducerDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ReducerPlaceBetDto(@NotNull Long bettingId, @NotNull @PositiveOrZero BigDecimal stake, Integer bonusSlip) {
}
