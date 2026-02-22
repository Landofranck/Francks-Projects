package project.application.port.out.bettingAccount;

import project.domain.model.BettingAccount;
import project.domain.model.Enums.BrokerType;

import java.util.List;

public interface ReadAllBettingAccountsPort {
    public List<BettingAccount> getAllBettingAcounts(BrokerType broker);
}
