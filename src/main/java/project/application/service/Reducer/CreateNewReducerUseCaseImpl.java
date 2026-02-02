package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.CreateNewReducerUseCase;
import project.application.port.out.Reducer.PersistReducerPort;
import project.domain.model.Reducer.Reducer;

@ApplicationScoped
public class CreateNewReducerUseCaseImpl implements CreateNewReducerUseCase {
    @Inject
    PersistReducerPort persistReducer;

    @Override
    public Long createNewReducer(Reducer reducer) {
        return persistReducer.persistReducerToDataBase(reducer);
    }
}
