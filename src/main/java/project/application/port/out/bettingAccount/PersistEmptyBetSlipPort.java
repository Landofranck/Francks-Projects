package project.application.port.out.bettingAccount;

import project.domain.model.DraftBetSlip;

public interface PersistEmptyBetSlipPort {
    void persistEmptySlip(Long bettingAccountId, DraftBetSlip betSlip);
}
