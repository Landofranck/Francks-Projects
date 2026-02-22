package project.application.port.in.Reducer;

import project.domain.model.Enums.BrokerType;
import project.domain.model.Reducer.Reducer;

import java.util.List;

public interface LoadAllReducdersUseCase {
    List<Reducer> loadReducers(BrokerType broker);
}
