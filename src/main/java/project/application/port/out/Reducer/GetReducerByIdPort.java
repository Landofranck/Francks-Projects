package project.application.port.out.Reducer;

import project.domain.model.Reducer.Reducer;

public interface GetReducerByIdPort {
    Reducer getReducer(Long id);
}
