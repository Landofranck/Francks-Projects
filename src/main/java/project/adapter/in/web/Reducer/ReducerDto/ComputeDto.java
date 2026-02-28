package project.adapter.in.web.Reducer.ReducerDto;

import jakarta.validation.Valid;

import java.util.List;

public record ComputeDto( List<@Valid BlockDto> specifications) {
}
