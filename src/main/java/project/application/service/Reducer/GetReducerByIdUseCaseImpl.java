package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.LoadReducerByIdUseCase;
import project.application.port.out.Reducer.ReadReducerByIdPort;
import project.domain.model.Match;
import project.domain.model.Reducer.Reducer;

@ApplicationScoped
public class GetReducerByIdUseCaseImpl implements LoadReducerByIdUseCase {
    @Inject
    ReadReducerByIdPort getReducer;

    @Override
    public Reducer loadReducer(Long id) {
        var out=getReducer.readReducer(id);

        return out;
    }
}
