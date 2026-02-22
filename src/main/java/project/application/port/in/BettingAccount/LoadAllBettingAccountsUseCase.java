package project.application.port.in.BettingAccount;

import project.domain.model.BettingAccount;
import project.domain.model.Enums.BrokerType;

import java.util.List;

public interface LoadAllBettingAccountsUseCase {
     List<BettingAccount> loadAllBettingAccounts(BrokerType broker);
}
