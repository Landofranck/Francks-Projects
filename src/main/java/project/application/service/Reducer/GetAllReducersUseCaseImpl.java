package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.LoadallReducerSummaryUseCase;
import project.application.port.in.Reducer.LoadAllReducdersUseCase;
import project.application.port.out.Reducer.ReadAllReducerSummaryPort;
import project.application.port.out.Reducer.ReadAllReducersPort;
import project.domain.model.Enums.BrokerType;
import project.domain.model.Reducer.Reducer;
import project.domain.model.Reducer.ReducerSummary;

import java.util.List;

@ApplicationScoped
public class GetAllReducersUseCaseImpl implements LoadAllReducdersUseCase, LoadallReducerSummaryUseCase {
    @Inject
    ReadAllReducersPort getAllReducers;
    @Inject
    ReadAllReducerSummaryPort readAllReducerSummary;
    @Override
    public List<Reducer> loadReducers(BrokerType broker) {
        return getAllReducers.readReducers(broker);
    }

    @Override
    public List<ReducerSummary> loadAllReducerSummary() {
        return readAllReducerSummary.readReducerSummaries();
    }
}
