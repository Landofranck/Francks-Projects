package project.application.port.out;

import project.domain.model.BettingAccount;

public interface UpdateBettingAccountBalancePort {
    void updateBalance(BettingAccount account);
}
