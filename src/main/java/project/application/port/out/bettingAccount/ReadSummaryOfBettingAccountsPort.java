package project.application.port.out.bettingAccount;

import project.domain.model.BettingAccount;

public interface ReadSummaryOfBettingAccountsPort {
    BettingAccount getSummaryAccount(Long id);
}
