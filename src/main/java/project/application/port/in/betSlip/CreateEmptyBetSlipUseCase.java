package project.application.port.in.betSlip;

import project.domain.model.BetSlip;
import project.domain.model.DraftBetSlip;
import project.domain.model.Enums.BetCategory;

public interface CreateEmptyBetSlipUseCase {
    void createEmpty(Long bettingAccountId, BetCategory category);
}
