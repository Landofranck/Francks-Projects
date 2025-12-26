package project.application.port.out;

import project.domain.model.BettingAccount;

public interface PersistBettingAccountPort {
    //the long id the account id
    public Long saveB(BettingAccount newAccount);
}
