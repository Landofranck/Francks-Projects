package project.application.port.in.BettingAccount;

import project.domain.model.BetSlip;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.BetStrategy;

import java.util.List;

public interface LoadBetHistoryUseCase {
    List<BetSlip> loadBetHistory(Long bettingId, BetStatus status, String matchKey, BetStrategy strategy);
}
