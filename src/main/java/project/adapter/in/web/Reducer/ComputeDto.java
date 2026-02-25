package project.adapter.in.web.Reducer;

import jakarta.validation.Valid;

import java.util.List;

public record ComputeDto( List<@Valid BlockDto> specifications) {
}
