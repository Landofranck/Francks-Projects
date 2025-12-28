package project.application.port.out.bettingAccount;

import project.domain.model.BettingAccount;

import java.util.List;

public interface ReadAllBettingAccountsPort {
    public List<BettingAccount> getAllBettingAcounts();
}
