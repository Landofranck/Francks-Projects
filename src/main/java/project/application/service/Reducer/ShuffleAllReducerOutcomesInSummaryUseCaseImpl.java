package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.LoadSummaryReducerUseCase;
import project.application.port.in.Reducer.ShuffelAllReducerOutcomesInSummaryUseCase;
import project.application.port.in.Reducer.ShuffleReducerMatchesUseCase;
import project.domain.model.Reducer.Reducer;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ShuffleAllReducerOutcomesInSummaryUseCaseImpl implements ShuffelAllReducerOutcomesInSummaryUseCase {
    @Inject
    ShuffleReducerMatchesUseCase shuffleReducerMatches;
    @Inject
    LoadSummaryReducerUseCase loadSummaryReducer;

    @Override
    public void shuffleAllReducerOutcomes(Long summaryId,Integer shuffleIds) {
        var out = loadSummaryReducer.loadReducer(summaryId);
        var reducerIds=new ArrayList<Long>();
        for (Reducer r : out.getReducers()) {
            reducerIds.add(r.getId());
        }
        for(Long id:reducerIds){
            shuffleReducerMatches.shuffle(id,shuffleIds);
        }
    }
}
