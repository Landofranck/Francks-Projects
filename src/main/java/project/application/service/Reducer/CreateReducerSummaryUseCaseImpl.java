package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.CreateReducerSummaryUseCase;
import project.application.port.out.PersistReducerSummaryPort;
import project.application.port.out.Reducer.PersistReducerPort;
import project.domain.model.Reducer.ReducerSummary;

@ApplicationScoped
public class CreateReducerSummaryUseCaseImpl implements CreateReducerSummaryUseCase {
    @Inject
    PersistReducerSummaryPort persistReducerSummary;

    @Override
    public Long createReducerSummary(ReducerSummary summary) {
        return persistReducerSummary.persistReducerSummary(summary);
    }
}
