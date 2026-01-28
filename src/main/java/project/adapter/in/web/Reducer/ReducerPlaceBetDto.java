package project.adapter.in.web.Reducer;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ReducerPlaceBetDto(@NotNull Long bettingId, @NotNull int slipIndex, BigDecimal stake) {
}
