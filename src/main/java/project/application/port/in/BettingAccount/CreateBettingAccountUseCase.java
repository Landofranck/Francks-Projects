package project.application.port.in.BettingAccount;

import project.domain.model.BettingAccount;

public interface CreateBettingAccountUseCase {
    void createNewBettingAccount(BettingAccount account);
}
