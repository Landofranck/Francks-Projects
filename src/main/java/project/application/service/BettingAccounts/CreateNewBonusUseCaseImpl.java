package project.application.service.BettingAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.BettingAccount.CreatNewBonusUseCase;
import project.application.port.out.bettingAccount.ReadBettingAccountByIdPort;
import project.application.port.out.bettingAccount.UpdateBettingAccountPort;
import project.domain.model.Bonus;

@ApplicationScoped
public class CreateNewBonusUseCaseImpl implements CreatNewBonusUseCase {
    @Inject
    ReadBettingAccountByIdPort getBettingAccount;
    @Inject
    UpdateBettingAccountPort updateBettingAccount;

    @Override
    public void createNewBonus(Long bettingAccountId, Bonus bonus) {
        var bet=getBettingAccount.getBettingAccount(bettingAccountId);
        bet.addBonus(bonus);
        updateBettingAccount.updateBettingAccount(bettingAccountId,bet);
    }
}
