package project.adapter.in.web.Reducer.ReducerDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateReducerSummaryDto(@NotNull @NotEmpty String description) {

}
