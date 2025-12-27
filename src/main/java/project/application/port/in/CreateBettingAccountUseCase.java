package project.application.port.in;

import project.domain.model.BettingAccount;

public interface CreateBettingAccountUseCase {
   public Long createNewBettingAccount(BettingAccount account);
}
