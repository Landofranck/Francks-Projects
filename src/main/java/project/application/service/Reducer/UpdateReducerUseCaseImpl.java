package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.UpdateUpdateUsecase;
import project.application.port.out.GetReducerByIdPort;
import project.application.port.out.UpdateReducerPort;
import project.domain.model.Reducer.Reducer;
@ApplicationScoped
public class UpdateReducerUseCaseImpl implements UpdateUpdateUsecase {
    @Inject
    UpdateReducerPort updateReducerPort;
    @Override
    public Reducer updateReducer(Long reducerId, Reducer update) {
        update.updateMatchVersion();
        return updateReducerPort.updateReducer(reducerId, update);

    }
}
