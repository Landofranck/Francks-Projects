package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.CreateNewReducerUseCase;
import project.application.port.out.PersistReducerPort;
import project.domain.model.Reducer;

@ApplicationScoped
public class CreateNewReducerUseCaseImpl implements CreateNewReducerUseCase {
    @Inject
    PersistReducerPort persistReducer;
    @Override
    public void createNewReducer(Reducer reducer) {
        persistReducer.persistReducerToDataBase(reducer);
    }
}
