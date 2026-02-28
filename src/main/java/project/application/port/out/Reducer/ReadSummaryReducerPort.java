package project.application.port.out.Reducer;

import project.domain.model.Reducer.ReducerSummary;

public interface ReadSummaryReducerPort {
    ReducerSummary getSummaryById(Long summaryId);
}
