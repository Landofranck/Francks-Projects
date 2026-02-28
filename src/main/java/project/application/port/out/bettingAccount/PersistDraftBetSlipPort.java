package project.application.port.out.bettingAccount;

import project.domain.model.DraftBetSlip;

public interface PersistDraftBetSlipPort {
    void persistDraftSlip(Long bettingAccountId, DraftBetSlip betSlip);
}
