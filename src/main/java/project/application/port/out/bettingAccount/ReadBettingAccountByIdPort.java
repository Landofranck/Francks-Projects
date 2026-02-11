package project.application.port.out.bettingAccount;

import project.domain.model.BettingAccount;

public interface ReadBettingAccountByIdPort {
    BettingAccount getBettingAccount(Long id);
}
