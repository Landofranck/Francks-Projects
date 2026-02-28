package project.adapter.in.web.Reducer.ReducerDto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AddAllMatchesDto(@NotNull List<Long> matchIds) {
}
