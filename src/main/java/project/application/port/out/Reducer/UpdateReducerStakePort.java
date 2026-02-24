package project.application.port.out.Reducer;

import project.domain.model.Money;

public interface UpdateReducerStakePort {
    void updateStake(Long id, Money stake, Money bonusStake);
}
