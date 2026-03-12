package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.AddAllMatchesToReducerUseCase;
import project.application.port.in.Reducer.AddMatchToReducerUseCase;
import project.application.port.out.Reducer.AddMatchToReducerPort;
import project.domain.model.Reducer.Reducer;

import java.util.List;

@ApplicationScoped
public class AddMatchToReducerUseCaseImpl implements AddMatchToReducerUseCase, AddAllMatchesToReducerUseCase {

    @Inject
    AddMatchToReducerPort addMatch;

    @Override
    public Reducer addMatchToReducer(Long reducerId, Long in) {
        return addMatch.addMatchToReducer(reducerId, in);
    }

    @Override
    public void addMatchesToReducer(Long reducerId, List<Long> matchIds) {
        for (Long matchId : matchIds) {
            addMatchToReducer(reducerId, matchId);
        }
    }
}
