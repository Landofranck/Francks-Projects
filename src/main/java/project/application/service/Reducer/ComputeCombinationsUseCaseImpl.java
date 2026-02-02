package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.ComputeCombinationUseCase;
import project.application.port.out.Reducer.GetReducerByIdPort;
import project.application.port.out.Reducer.UpdateReducerPort;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Reducer.Block;
import project.domain.model.Reducer.Reducer;

import java.util.List;

@ApplicationScoped
public class ComputeCombinationsUseCaseImpl implements ComputeCombinationUseCase {
    @Inject
    GetReducerByIdPort getReducer;
    @Inject
    UpdateReducerPort updateReducer;

    @Override
    public Reducer computeCombination(Long id, List<Block> specifications) {
        var reducer = getReducer.getReducer(id);
        reducer.setBlocks(specifications);
        reducer.computeSlips(BetCategory.SINGLE);
        var out = updateReducer.updateReducer(id, reducer);
        return out;
    }
}
