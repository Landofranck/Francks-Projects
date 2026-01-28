package project.application.port.in.Reducer;

import project.domain.model.Reducer.Block;
import project.domain.model.Reducer.Reducer;

import java.util.List;

public interface ComputeCombinationUseCase {
    Reducer computeCombination(Long id, List<Block> specifications);
}
