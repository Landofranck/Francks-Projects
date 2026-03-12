package project.adapter.in.web.Reducer.ReducerDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record ComputeSummaryDto(@NotNull BigDecimal newBalance, @NotNull List<@Valid BlockDto> specifications) {
}
