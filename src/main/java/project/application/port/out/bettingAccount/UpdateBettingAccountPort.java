package project.application.port.out.bettingAccount;

import project.domain.model.BettingAccount;

public interface UpdateBettingAccountPort {
    void updateBettingAccount(Long bettingId, BettingAccount updated);
}
