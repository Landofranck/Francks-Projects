package project.application.port.out.Reducer;

import project.domain.model.Enums.BrokerType;
import project.domain.model.Reducer.Reducer;

import java.util.List;

public interface ReadAllReducersPort {
    List<Reducer> readReducers(BrokerType broker);
}
