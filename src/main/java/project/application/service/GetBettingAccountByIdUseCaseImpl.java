package project.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.LoadBettingAccountByIdUsecase;
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
