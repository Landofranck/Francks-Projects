package project.application.port.in.betSlip;

import project.domain.model.BetSlip;

public interface AddEventPickToBetSlipUseCase {
    project.domain.model.BetSlip addPick(BetSlip slip, Long matchId, String outcomeName);
}
