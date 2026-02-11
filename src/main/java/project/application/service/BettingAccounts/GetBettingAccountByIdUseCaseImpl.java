package project.application.service.BettingAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.BettingAccount.LoadBettingAccountByIdUsecase;
import project.application.port.out.bettingAccount.ReadBettingAccountByIdPort;
import project.domain.model.BettingAccount;

@ApplicationScoped
public class GetBettingAccountByIdUseCaseImpl implements LoadBettingAccountByIdUsecase {
    @Inject
    ReadBettingAccountByIdPort readBettingAccount;

    @Override
    public BettingAccount loadAccount(Long id) {
        return readBettingAccount.getBettingAccount(id);
    }
}
