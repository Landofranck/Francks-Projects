package project.application.service.BettingAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.BettingAccount.CreateBettingAccountUseCase;
import project.application.port.out.bettingAccount.PersistBettingAccountPort;
import project.application.service.betSlip.CreateEmptyBetSlipUseCaseImpl;
import project.domain.model.BettingAccount;
import project.domain.model.Enums.BetCategory;

import java.util.Objects;

@ApplicationScoped
public class CreateBettingAccountUseCaseImpl implements CreateBettingAccountUseCase {

    @Inject
    PersistBettingAccountPort persistBettingAccountPort;

    @Override
    public void createNewBettingAccount(BettingAccount account) {
        Long id= persistBettingAccountPort.saveBettingAccount(account);
    }
}
