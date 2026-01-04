package project.application.port.in.betSlip;

import project.domain.model.DraftBetSlip;

public interface RemoveAllPicksFromDraftSlipUseCase {
    public DraftBetSlip removeAllPicks(Long bettingId);
}
