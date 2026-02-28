package project.adapter.in.web.Reducer.ReducerDto;

import project.adapter.in.web.Utils.Link;

import java.util.List;

public record GetAllReducerDto(List<ReadReducerDto> allReducers, List<Link> links) {
}
