package project.application.service.betSlip;

import jakarta.inject.Inject;
import project.application.port.in.BettingAccount.RemovePickByNumberUseCase;
import project.application.port.out.bettingAccount.PersistEmptyBetSlipPort;
import project.application.port.out.bettingAccount.ReadEmptSlipByParenPort;
import project.domain.model.DraftBetSlip;

public class RemovePickByNumberImpl implements RemovePickByNumberUseCase {
    @Inject
    ReadEmptSlipByParenPort readEmptSlip;
    @Inject
    PersistEmptyBetSlipPort putBetSlip;

    @Override
    public DraftBetSlip removeSpecifiedPick(Long bettingAccountId, int pickIndex) {
        var slip = readEmptSlip.getAvailableBettingSlip(bettingAccountId);
        if (slip == null) {
            throw new IllegalArgumentException("no betslip found 27");
        }

        if (slip.getDraftSlipOwner() == null || slip.getDraftSlipOwner().getAccountId() == null) {
            throw new IllegalArgumentException("BetSlip must have a parent betting account before adding picks addpickimpl 27");
        }
        if (!slip.getDraftSlipOwner().getAccountId().equals(bettingAccountId)) {
            throw new IllegalArgumentException("BetSlip does not belong to betting account " + bettingAccountId);
        }
        if (slip == null) {
            throw new IllegalArgumentException("bet slip must not be null");
        }

        slip.removeMatchEventPicksByIndex(pickIndex);
        putBetSlip.persistEmptyslip(bettingAccountId, slip);
        // Recalculate slip odds if you store total odds
        // (depends on your BetSlip model)
        // slip.recalculateTotalOdds();

        return slip;
    }


}
