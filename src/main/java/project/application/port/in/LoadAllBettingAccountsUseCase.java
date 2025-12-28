package project.application.port.in;

import project.domain.model.Account;
import project.domain.model.BettingAccount;

import java.util.List;

public interface LoadAllBettingAccountsUseCase {
    public List<BettingAccount> loadAllBettingAccounts();
}
