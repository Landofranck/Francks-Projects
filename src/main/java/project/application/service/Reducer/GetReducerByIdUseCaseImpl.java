package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.LoadReducerByIdUseCase;
import project.application.port.out.Reducer.GetReducerByIdPort;
import project.domain.model.Reducer.Reducer;

@ApplicationScoped
public class GetReducerByIdUseCaseImpl implements LoadReducerByIdUseCase {
    @Inject
    GetReducerByIdPort getReducer;

    @Override
    public Reducer loadReducer(Long id) {
        return getReducer.getReducer(id);
    }
}
