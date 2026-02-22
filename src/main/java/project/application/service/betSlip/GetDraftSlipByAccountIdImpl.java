package project.application.service.betSlip;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.betSlip.LoadDraftBetSlipByParentUseCase;
import project.application.port.out.bettingAccount.ReadEmptSlipByParenPort;
import project.domain.model.DraftBetSlip;

/**
 * returns empty slip of a specified account
 */
@ApplicationScoped
public class GetDraftSlipByAccountIdImpl implements LoadDraftBetSlipByParentUseCase {
    @Inject
    ReadEmptSlipByParenPort readParentSlip;

    @Override
    public DraftBetSlip loadSlipFromParents(Long parentId) {
        return readParentSlip.getAvailableBettingSlip(parentId);
    }
}
