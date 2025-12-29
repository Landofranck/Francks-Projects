package project.application.port.out.bettingAccount;

import project.domain.model.DraftBetSlip;

public interface ReadEmptSlipByParenPort {
    public DraftBetSlip getAvailableBettingSlip(Long parentAccountId);
}
