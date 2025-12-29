package project.application.port.out.bettingAccount;

import project.domain.model.BetSlip;

public interface PersistEmptyBetSlipPort {
    Long persisEmptyslip(Long bettingAccountId, BetSlip betSlip);
}
