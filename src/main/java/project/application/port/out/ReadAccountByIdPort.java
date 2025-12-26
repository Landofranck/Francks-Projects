package project.application.port.out;

import project.domain.model.BettingAccount;

public interface ReadAccountByIdPort {
    public BettingAccount getAccount(Long id);
}
