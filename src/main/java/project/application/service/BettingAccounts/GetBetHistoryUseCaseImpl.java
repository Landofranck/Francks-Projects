package project.application.service.BettingAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.BettingAccount.LoadBetHistoryUseCase;
import project.application.port.out.bettingAccount.ReadBettingHistoryPort;
import project.domain.model.BetSlip;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.BetStrategy;

import java.util.List;

@ApplicationScoped
public class GetBetHistoryUseCaseImpl implements LoadBetHistoryUseCase {
    @Inject
    ReadBettingHistoryPort readBettingHistory;

    @Override
    public List<BetSlip> loadBetHistory(Long bettingId, BetStatus status, String matchKey, BetStrategy strategy) {
        return readBettingHistory.readBetHistory(bettingId,status,matchKey,strategy);
    }
}
