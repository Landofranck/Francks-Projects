package project.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.CreateBettingAccountUseCase;
import project.application.port.out.bettingAccount.PersistBettingAccountPort;
import project.domain.model.BettingAccount;

import java.util.Objects;

@ApplicationScoped
public class CreateBettingAccountUseCaseImpl implements CreateBettingAccountUseCase {

    @Inject
    PersistBettingAccountPort persistBettingAccountPort;

    @Override
    public Long createNewBettingAccount(BettingAccount account) {
        Objects.requireNonNull(account, "account");
        var newAccount=new BettingAccount(account.getAccountName(),account.getBrokerType());
        return persistBettingAccountPort.saveBettingAccount(newAccount);
    }
}
