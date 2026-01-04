package project.application.port.in.betSlip;

import project.domain.model.DraftBetSlip;

public interface AddEventPickToBetSlipUseCase {
    DraftBetSlip addPick(Long bettingAccountId, Long matchId, String outcomeName);
}
