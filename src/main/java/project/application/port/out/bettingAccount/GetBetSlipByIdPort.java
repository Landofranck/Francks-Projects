package project.application.port.out.bettingAccount;

import project.domain.model.BetSlip;

public interface GetBetSlipByIdPort {
    BetSlip getBetSlip(Long id);
}
