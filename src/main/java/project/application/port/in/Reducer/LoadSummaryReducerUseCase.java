package project.application.port.in.Reducer;

import project.domain.model.Reducer.ReducerSummary;

public interface LoadSummaryReducerUseCase {
    ReducerSummary loadReducer(Long summaryId);
}
