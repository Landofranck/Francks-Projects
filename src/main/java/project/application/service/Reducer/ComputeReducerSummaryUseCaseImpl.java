package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.AddReducerToSummaryUseCase;
import project.application.port.in.Reducer.ComputeCombinationUseCase;
import project.application.port.in.Reducer.ComuteReducerSummaryUseCase;
import project.application.port.out.Reducer.AddReducerToSummaryPort;
import project.application.port.out.Reducer.ReadSummaryReducerPort;
import project.domain.model.Reducer.Block;
import project.domain.model.Reducer.Reducer;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ComputeReducerSummaryUseCaseImpl implements ComuteReducerSummaryUseCase, AddReducerToSummaryUseCase {
    @Inject
    ComputeCombinationUseCase computeCombinationUseCase;
    @Inject
    ReadSummaryReducerPort readSummaryReducer;
    @Inject
    AddReducerToSummaryPort addReducerToSummaryPort;

    @Override
    public void comuteReducerSummary(Long summaryId, List<Block> blocks) {
        var sm = readSummaryReducer.getSummaryById(summaryId);
        var reducerIds = new ArrayList<Long>();
        for (Reducer r : sm.getReducers()) {
            reducerIds.add(r.getId());
        }
        for (Long reducerId : reducerIds) {
            computeCombinationUseCase.computeCombination(reducerId, blocks);
        }
    }

    @Override
    public void addReducerToSummary(Long summaryId, Long reducerId) {
        addReducerToSummaryPort.addReducerToSummary(summaryId,reducerId);
    }
}
