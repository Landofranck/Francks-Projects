package project.application.port.in.Reducer;

import project.domain.model.Reducer.Reducer;

public interface RefreshReducerUseCase {
    Reducer refreshReducerById(Long reducerId);
}
