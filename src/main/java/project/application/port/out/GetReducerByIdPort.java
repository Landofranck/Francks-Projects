package project.application.port.out;

import project.domain.model.Reducer.Reducer;

public interface GetReducerByIdPort {
    Reducer getReducer(Long id);
}
