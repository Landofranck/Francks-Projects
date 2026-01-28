package project.application.service.betSlip;

import jakarta.inject.Inject;
import project.application.port.in.betSlip.LoadDraftBetSlipByParentUseCase;
import project.application.port.out.bettingAccount.ReadEmptSlipByParenPort;
import project.domain.model.DraftBetSlip;

/**
 * returns empty slip of a specified account
 */
public class GetDraftSlipByAccountIdImpl implements LoadDraftBetSlipByParentUseCase {
   @Inject
    ReadEmptSlipByParenPort readParentSlip;

    @Override
    public DraftBetSlip loadSlipFromParents(Long parentId) {
        var slip=readParentSlip.getAvailableBettingSlip(parentId);
        if (slip==null) throw new IllegalArgumentException("no such parent in data base getemptslipby ie line 18");
        return slip;
    }
}
