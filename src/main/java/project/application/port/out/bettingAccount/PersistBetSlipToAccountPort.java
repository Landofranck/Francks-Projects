package project.application.port.out.bettingAccount;

import project.domain.model.BetSlip;
import project.domain.model.DraftBetSlip;
import project.domain.model.Enums.BetStrategy;

public interface PersistBetSlipToAccountPort {
    Long persistSlipToAccount(Long bettingAccountId, DraftBetSlip slip, BetStrategy bettingStrategy);
}
