package project.application.port.out.Reducer;

import project.domain.model.Reducer.Reducer;

public interface AddMatchToReducerPort {
    Reducer addMatchToReducer(Long reducerId,Long matchId);
}
