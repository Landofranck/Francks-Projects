package project.application.service.BettingAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.BettingAccount.UpdateMatchBonusUseCase;
import project.application.port.out.bettingAccount.UpdateMatchBonusPort;

@ApplicationScoped
public class UpdateMatchBonusUseCaseImpl implements UpdateMatchBonusUseCase {
    @Inject
    UpdateMatchBonusPort updateMatchBonus;

    @Override
    public void updateMatchBonus(Long matchId) {
        updateMatchBonus.updateMatchBonus(matchId);
    }
}
