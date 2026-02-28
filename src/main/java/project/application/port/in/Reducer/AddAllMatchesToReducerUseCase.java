package project.application.port.in.Reducer;

import java.util.List;

public interface AddAllMatchesToReducerUseCase {
    void addMatchesToReducer(Long reducerId, List<Long> matchIds);
}
