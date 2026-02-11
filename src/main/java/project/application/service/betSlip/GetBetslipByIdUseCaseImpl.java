package project.application.service.betSlip;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.betSlip.GetBetSlipByIdsecase;
import project.application.port.out.bettingAccount.GetBetSlipByIdPort;
import project.domain.model.BetSlip;

@ApplicationScoped
public class GetBetslipByIdUseCaseImpl implements GetBetSlipByIdsecase {
    @Inject
    GetBetSlipByIdPort getSlip;
    @Override
    public BetSlip getBetSlip(Long id) {
        return getSlip.getBetSlip(id);
    }
}
