package project.application.port.out.bettingAccount;

import project.domain.model.BettingAccount;

public interface ReadBettingAccountByIdPort {
    public BettingAccount getBettingAccount(Long id);
}
