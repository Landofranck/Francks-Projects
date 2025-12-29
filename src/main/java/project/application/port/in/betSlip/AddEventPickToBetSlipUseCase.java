package project.application.port.in.betSlip;

import project.domain.model.BetSlip;

public interface AddEventPickToBetSlipUseCase {
    BetSlip addPick(Long bettingAccountId, Long matchId, String outcomeName);
}
