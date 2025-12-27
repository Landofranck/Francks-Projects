package project.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.CreateBettingAccountUseCase;
import project.application.port.out.PersistBettingAccountPort;
import project.domain.model.BettingAccount;

import java.util.Objects;

@ApplicationScoped
public class CreateBettingAccountUseCaseImpl implements CreateBettingAccountUseCase {

    @Inject
    PersistBettingAccountPort persistBettingAccountPort;

    @Override
    public Long createNewBettingAccount(BettingAccount account) {
        Objects.requireNonNull(account, "account");
        return persistBettingAccountPort.saveBettingAccount(account);
    }
}
