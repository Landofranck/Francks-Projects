package project.application.port.in.Reducer;

import project.domain.model.Money;
import project.domain.model.Reducer.Reducer;


public interface PlaceBetFromReducerUseCase {
    Reducer placeBetFromReducer(Long reducerId, Long bettingId, int slipNumber, Money stake);
}
