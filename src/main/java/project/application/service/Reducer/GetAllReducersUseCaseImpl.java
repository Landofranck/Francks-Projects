package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.adapter.in.web.Reducer.GetAllReducerDto;
import project.application.port.in.Reducer.LoadAllReducdersUseCase;
import project.application.port.out.Reducer.GetAllReducersPort;
import project.domain.model.Enums.BrokerType;
import project.domain.model.Reducer.Reducer;

import java.util.List;

@ApplicationScoped
public class GetAllReducersUseCaseImpl implements LoadAllReducdersUseCase {
    @Inject
    GetAllReducersPort getAllReducers;
    @Override
    public List<Reducer> loadReducers(BrokerType broker) {
        return getAllReducers.getReducers(broker);
    }
}
