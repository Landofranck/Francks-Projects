package project.adapter.in.web.Reducer.ReducerDto;

import project.adapter.in.web.Utils.Link;

import java.util.List;

public record AllReducerSummaryDto (List<ReadReducerSummaryDto> reducerDtos,List<Link> links){

}
