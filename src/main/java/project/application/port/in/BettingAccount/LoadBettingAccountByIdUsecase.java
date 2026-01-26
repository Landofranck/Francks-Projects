package project.application.port.in.BettingAccount;

import project.domain.model.BettingAccount;

public interface LoadBettingAccountByIdUsecase {
    public BettingAccount loadAccount(Long id);
}
