package project.application.port.in;

import project.domain.model.Account;
import project.domain.model.BettingAccount;

public interface LoadBettingAccountByIdUsecase {
    public BettingAccount loadAccount(Long id);
}
