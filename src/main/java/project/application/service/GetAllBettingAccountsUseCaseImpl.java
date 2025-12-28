package project.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.LoadAllBettingAccountsUseCase;
import project.application.port.out.ReadAllBettingAccountsPort;
import project.domain.model.BettingAccount;

import java.util.List;

@ApplicationScoped
public class GetAllBettingAccountsUseCaseImpl implements LoadAllBettingAccountsUseCase {

    @Inject
    ReadAllBettingAccountsPort readAll;

    @Override
    public List<BettingAccount> loadAllBettingAccounts() {
        return readAll.getAllBettingAcounts();
    }
}
