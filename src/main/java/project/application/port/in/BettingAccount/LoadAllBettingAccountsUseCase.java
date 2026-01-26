package project.application.port.in.BettingAccount;

import project.domain.model.BettingAccount;

import java.util.List;

public interface LoadAllBettingAccountsUseCase {
     List<BettingAccount> loadAllBettingAccounts();
}
