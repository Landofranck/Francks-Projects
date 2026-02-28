package project.application.port.in.Reducer;

import project.domain.model.Reducer.Block;

import java.util.List;

public interface ComuteReducerSummaryUseCase {
    void comuteReducerSummary(Long summaryId, List<Block> blocks);
}
