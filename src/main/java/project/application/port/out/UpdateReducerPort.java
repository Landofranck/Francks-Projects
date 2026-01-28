package project.application.port.out;

import project.domain.model.Reducer.Reducer;

public interface UpdateReducerPort {
    Reducer updateReducer(Long reducerId, Reducer update);
}
