package project.application.port.out.Reducer;

import project.domain.model.Reducer.Reducer;

public interface RefreshReducerByIdPort {
    Reducer refreshReducer(Long id, Reducer refreshed);
}
