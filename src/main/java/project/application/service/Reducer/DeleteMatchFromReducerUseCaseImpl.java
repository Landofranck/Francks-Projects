package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.DeleteMatchFromReducerUseCase;
import project.application.port.out.DeleteMatchFromReducerPort;

@ApplicationScoped
public class DeleteMatchFromReducerUseCaseImpl implements DeleteMatchFromReducerUseCase {
    @Inject
    DeleteMatchFromReducerPort deleteMatchFromReducer;

    @Override
    public void deletMatchFromReducer(Long reducerId, Long matchId) {
        deleteMatchFromReducer.deleteMatch(reducerId, matchId);
    }
}
