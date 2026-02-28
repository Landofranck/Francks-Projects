package project.application.port.in.Reducer;

import project.domain.model.Reducer.ReducerSummary;

public interface CreateReducerSummaryUseCase {
    Long createReducerSummary(ReducerSummary summary);
}
