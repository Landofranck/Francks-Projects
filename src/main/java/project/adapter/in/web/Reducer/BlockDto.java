package project.adapter.in.web.Reducer;

import project.domain.model.Enums.BlockType;

public record BlockDto(BlockType type, int start, int end) {
}
