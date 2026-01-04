package project.application.port.out.bettingAccount;

import project.domain.model.BetSlip;
import project.domain.model.DraftBetSlip;

public interface PersistBetSlipToAccountPort {
    Long persistSlipToAccount(Long bettingAccountId, DraftBetSlip slip, String bettingStrategy);
}
