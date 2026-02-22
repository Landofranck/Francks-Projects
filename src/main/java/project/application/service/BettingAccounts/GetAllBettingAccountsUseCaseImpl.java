package project.application.service.BettingAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.BettingAccount.LoadAllBettingAccountsUseCase;
import project.application.port.out.bettingAccount.ReadAllBettingAccountsPort;
import project.domain.model.BettingAccount;
import project.domain.model.Enums.BrokerType;

import java.util.List;

@ApplicationScoped
public class GetAllBettingAccountsUseCaseImpl implements LoadAllBettingAccountsUseCase {

    @Inject
    ReadAllBettingAccountsPort readAll;

    @Override
    public List<BettingAccount> loadAllBettingAccounts(BrokerType broker) {
        return readAll.getAllBettingAcounts(broker);
    }
}
