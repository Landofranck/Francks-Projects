package project.application.port.out.bettingAccount;

import project.domain.model.BetSlip;

public interface PersistBetSlipToAccountPort {
    Long persistSlipToAccount(Long bettingAccountId, BetSlip slip);
}
