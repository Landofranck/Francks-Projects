package project.application.port.out;

import project.domain.model.Reducer.Reducer;

public interface PersistReducerPort {
    Long persistReducerToDataBase(Reducer reducer);
}
