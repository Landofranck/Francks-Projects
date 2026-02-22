package project.application.port.out.bettingAccount;

import project.domain.model.BetSlip;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.BetStrategy;

import java.util.List;

public interface ReadBettingHistoryPort {
    List<BetSlip> readBetHistory(Long bettingId, BetStatus status, String matchkey, BetStrategy strategy);
}
