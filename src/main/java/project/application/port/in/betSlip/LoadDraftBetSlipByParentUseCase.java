package project.application.port.in.betSlip;

import project.domain.model.DraftBetSlip;

public interface LoadDraftBetSlipByParentUseCase {
     DraftBetSlip loadSlipFromParents(Long parentId);
}
