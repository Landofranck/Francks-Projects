package project.application.port.in.betSlip;

import project.domain.model.BetSlip;
import project.domain.model.DraftBetSlip;

public interface CreateEmptyBetSlipUseCase {
    DraftBetSlip createEmpty(Long bettingAccountId, String category);
}
