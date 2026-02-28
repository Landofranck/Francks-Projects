package project.application.port.out.bettingAccount;

import project.domain.model.BetSlip;

public interface ReadBetSlipByIdPort {
    BetSlip readBetSlip(Long id);
}
