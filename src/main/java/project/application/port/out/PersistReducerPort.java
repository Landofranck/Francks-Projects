package project.application.port.out;

import project.domain.model.Reducer;

public interface PersistReducerPort {
    void persistReducerToDataBase(Reducer reducer);
}
