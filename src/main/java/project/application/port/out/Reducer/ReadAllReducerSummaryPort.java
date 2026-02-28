package project.application.port.out.Reducer;

import project.domain.model.Reducer.ReducerSummary;

import java.util.List;

public interface ReadAllReducerSummaryPort {
    List<ReducerSummary> readReducerSummaries();
}
