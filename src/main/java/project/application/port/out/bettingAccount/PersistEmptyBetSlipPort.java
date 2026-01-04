package project.application.port.out.bettingAccount;

import project.domain.model.BetSlip;
import project.domain.model.DraftBetSlip;

public interface PersistEmptyBetSlipPort {
    DraftBetSlip persistEmptyslip(Long bettingAccountId, DraftBetSlip betSlip);
}
