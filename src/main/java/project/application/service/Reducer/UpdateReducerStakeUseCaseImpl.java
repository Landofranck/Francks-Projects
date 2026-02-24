package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.UpdateReducerStakeUseCase;
import project.application.port.out.Reducer.UpdateReducerStakePort;
import project.domain.model.Money;

import java.math.BigDecimal;

@ApplicationScoped
public class UpdateReducerStakeUseCaseImpl implements UpdateReducerStakeUseCase {
    @Inject
    UpdateReducerStakePort updateReducerStake;

    @Override
    public void updateReducerStake(Long id, BigDecimal stake, BigDecimal bonusStake) {
        if (bonusStake == null)
            bonusStake = BigDecimal.ZERO;
        updateReducerStake.updateStake(id, new Money(stake), new Money(bonusStake));
    }
}
