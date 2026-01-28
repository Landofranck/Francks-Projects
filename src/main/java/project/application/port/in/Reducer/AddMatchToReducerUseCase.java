package project.application.port.in.Reducer;


import project.domain.model.Reducer.Reducer;

public interface AddMatchToReducerUseCase {
    Reducer addMatchToReducer(Long reducerId, Long in);
}
