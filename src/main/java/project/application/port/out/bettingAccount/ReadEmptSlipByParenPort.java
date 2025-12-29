package project.application.port.out.bettingAccount;

import project.domain.model.BetSlip;

public interface ReadEmptSlipByParenPort {
    public BetSlip getAvailableBettingSlip(Long parentAccountId);
}
