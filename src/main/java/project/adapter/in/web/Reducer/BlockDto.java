package project.adapter.in.web.Reducer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import project.domain.model.Enums.BlockType;

public record BlockDto(@NotNull BlockType type, @NotNull @PositiveOrZero Integer start, @NotNull @PositiveOrZero Integer end) {
}
