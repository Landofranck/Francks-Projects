package project.application.port.in.Reducer;

import project.domain.model.Reducer.Reducer;

public interface UpdateUpdateUsecase {
    Reducer updateReducer(Long ReducerId, Reducer update);
}
