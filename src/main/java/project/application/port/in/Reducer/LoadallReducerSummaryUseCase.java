package project.application.port.in.Reducer;

import project.domain.model.Reducer.ReducerSummary;

import java.util.List;

public interface LoadallReducerSummaryUseCase {
    List<ReducerSummary> loadAllReducerSummary();
}
