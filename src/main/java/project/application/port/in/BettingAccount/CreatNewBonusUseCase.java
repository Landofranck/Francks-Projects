package project.application.port.in.BettingAccount;

import project.domain.model.Bonus;

public interface CreatNewBonusUseCase {
    void createNewBonus(Long bettingAccountId, Bonus bonus);
}
