package project.application.port.out;

import project.domain.model.Account;
import project.domain.model.BettingAccount;

import java.util.List;

public interface ReadAllBettingAccountsPort {
    public List<BettingAccount> getAllBettingAcounts();
}
