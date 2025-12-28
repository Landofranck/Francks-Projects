package project.application.port.out.bettingAccount;

import project.domain.model.BettingAccount;

public interface UpdateBettingAccountBalancePort {
    void updateBalance(BettingAccount account);
}
