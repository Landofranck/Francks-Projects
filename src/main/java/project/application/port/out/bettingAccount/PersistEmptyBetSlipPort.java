package project.application.port.out.bettingAccount;

import project.domain.model.BetSlip;
import project.domain.model.DraftBetSlip;

public interface PersistEmptyBetSlipPort {
    Long persistEmptyslip(Long bettingAccountId, DraftBetSlip betSlip);
}
