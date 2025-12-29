package project.application.port.in.betSlip;

import project.domain.model.BetSlip;
import project.domain.model.DraftBetSlip;

public interface LoadEmptyBetSlipByParentUseCase {
    public DraftBetSlip loadSlipFromParents(Long parentId);
}
