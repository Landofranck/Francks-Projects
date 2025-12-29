package project.application.port.in.betSlip;

import project.domain.model.BetSlip;

public interface LoadEmptyBetSlipByParentUseCase {
    public BetSlip loadSlipFromParents(Long parentId);
}
