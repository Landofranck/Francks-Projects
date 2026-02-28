package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.AddAllMatchesToReducerUseCase;
import project.application.port.in.Reducer.ClearAllMatchesFromReducerUseCase;
import project.application.port.in.Reducer.LoadReducerByIdUseCase;
import project.application.port.in.Reducer.ShuffleReducerMatchesUseCase;

@ApplicationScoped
public class ShuffleReducerMatchesUseCaseImpl implements ShuffleReducerMatchesUseCase {
    @Inject
    ClearAllMatchesFromReducerUseCase clearMatchesFromReducer;
    @Inject
    AddAllMatchesToReducerUseCase addAllMatchesToReducerUseCase;
    @Inject
    LoadReducerByIdUseCase loadReducerByIdUseCase;

    @Override
    public void shuffle(Long reducerId, Integer index) {
        var out = loadReducerByIdUseCase.loadReducer(reducerId);
        out.checkSchuffle();
        var shuffleOrder = out.getShuffleCombinations().get(index);
        clearMatchesFromReducer.clearAllMatchesFromReducer(reducerId);
        addAllMatchesToReducerUseCase.addMatchesToReducer(reducerId,shuffleOrder.matchIds());
    }
}
