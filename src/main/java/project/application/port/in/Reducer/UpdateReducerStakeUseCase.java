package project.application.port.in.Reducer;

import java.math.BigDecimal;

public interface UpdateReducerStakeUseCase {
    void updateReducerStake(Long id, BigDecimal stake, BigDecimal bonusStake);
}
