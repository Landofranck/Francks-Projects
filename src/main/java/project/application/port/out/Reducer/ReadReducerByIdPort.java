package project.application.port.out.Reducer;

import project.domain.model.Reducer.Reducer;

public interface ReadReducerByIdPort {
    Reducer readReducer(Long id);
}
