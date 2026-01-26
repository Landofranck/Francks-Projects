package project.application.port.in.BettingAccount;

import project.domain.model.DraftBetSlip;

public interface RemovePickByNumberUseCase {
    DraftBetSlip removeSpecifiedPick(Long bettingId, int pickIndex);
}
