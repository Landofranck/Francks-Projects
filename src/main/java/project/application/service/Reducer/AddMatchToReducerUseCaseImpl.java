package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.AddAllMatchesToReducerUseCase;
import project.application.port.in.Reducer.AddMatchToReducerUseCase;
import project.application.port.out.Reducer.AddMatchToReducerPort;
import project.application.port.out.Reducer.UpdateReducerPort;
import project.domain.model.Reducer.Reducer;

import java.util.List;

@ApplicationScoped
public class AddMatchToReducerUseCaseImpl implements AddMatchToReducerUseCase, AddAllMatchesToReducerUseCase {

    @Inject
    AddMatchToReducerPort addMatchToReducer;
    @Inject
    UpdateReducerPort updateReducer;

    @Override
    public Reducer addMatchToReducer(Long reducerId, Long in) {
        var out = addMatchToReducer.addMatchToReducer(reducerId, in);
        out.checkSchuffle();
        updateReducer.updateReducer(reducerId, out);
        return out;
    }

    @Override
    public void addMatchesToReducer(Long reducerId, List<Long> matchIds) {
        for (Long matchId : matchIds) {
            addMatchToReducer.addMatchToReducer(reducerId, matchId);
        }
    }
}
