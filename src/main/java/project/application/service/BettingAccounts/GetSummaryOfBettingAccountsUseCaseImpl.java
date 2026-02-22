package project.application.service.BettingAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.BettingAccount.LoadBettingSummaryOfBettingAccountUseCase;
import project.application.port.out.bettingAccount.ReadSummaryOfBettingAccountsPort;
import project.domain.model.BettingAccount;

@ApplicationScoped
public class GetSummaryOfBettingAccountsUseCaseImpl implements LoadBettingSummaryOfBettingAccountUseCase {
    @Inject
    ReadSummaryOfBettingAccountsPort getSummaryAccount;
    @Override
    public BettingAccount laadBettingAccount(Long id) {
        return getSummaryAccount.getSummaryAccount(id);
    }
}
