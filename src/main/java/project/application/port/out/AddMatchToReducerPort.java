package project.application.port.out;

import project.domain.model.Reducer;

public interface AddMatchToReducerPort {
    Reducer addMatchToReducer(Long reducerId,Long matchId);
}
