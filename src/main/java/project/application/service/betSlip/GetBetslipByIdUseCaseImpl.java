package project.application.service.betSlip;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.betSlip.GetBetSlipByIdsecase;
import project.application.port.out.bettingAccount.ReadBetSlipByIdPort;
import project.domain.model.BetSlip;

@ApplicationScoped
public class GetBetslipByIdUseCaseImpl implements GetBetSlipByIdsecase {
    @Inject
    ReadBetSlipByIdPort getSlip;
    @Override
    public BetSlip getBetSlip(Long id) {
        return getSlip.readBetSlip(id);
    }
}
