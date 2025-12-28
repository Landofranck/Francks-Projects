package project.application.port.in.betSlip;

import project.domain.model.BetSlip;

public interface CreateEmptyBetSlipUseCase {
    BetSlip createEmpty(Long bettingAccountId, String category);
}
