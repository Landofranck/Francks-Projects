package project.application.port.out.bettingAccount;

import project.domain.model.BettingAccount;

public interface PersistBettingAccountPort {
    //the long id the account id
    public Long saveBettingAccount(BettingAccount newAccount);
}
