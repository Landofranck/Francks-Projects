package project.application.port.out;

import project.domain.model.Reducer.ReducerSummary;

public interface PersistReducerSummaryPort {
    Long persistReducerSummary(ReducerSummary summary);
}
