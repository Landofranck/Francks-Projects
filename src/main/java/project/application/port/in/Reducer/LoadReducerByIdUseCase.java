package project.application.port.in.Reducer;

import project.domain.model.Reducer.Reducer;

public interface LoadReducerByIdUseCase {
    Reducer loadReducer(Long id);
}
