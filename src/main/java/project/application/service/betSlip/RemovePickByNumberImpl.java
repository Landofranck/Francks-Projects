package project.application.service.betSlip;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.BettingAccount.RemovePickByNumberUseCase;
import project.application.port.out.bettingAccount.PersistEmptyBetSlipPort;
import project.application.port.out.bettingAccount.ReadEmptSlipByParenPort;
import project.domain.model.DraftBetSlip;

@ApplicationScoped
public class RemovePickByNumberImpl implements RemovePickByNumberUseCase {
    @Inject
    ReadEmptSlipByParenPort readEmptSlip;
    @Inject
    PersistEmptyBetSlipPort putBetSlip;

    @Override
    public DraftBetSlip removeSpecifiedPick(Long bettingAccountId, int pickIndex) {
        var slip = readEmptSlip.getAvailableBettingSlip(bettingAccountId);
        if (slip.getDraftSlipOwner() == null || slip.getDraftSlipOwner().getAccountId() == null) {
            throw new IllegalArgumentException("ReducerBetSlip must have a parent betting account before adding picks addpickimpl 27");
        }
        if (!slip.getDraftSlipOwner().getAccountId().equals(bettingAccountId)) {
            throw new IllegalArgumentException("ReducerBetSlip does not belong to betting account " + bettingAccountId);
        }

        slip.removeMatchEventPicksByIndex(pickIndex);
        putBetSlip.persistEmptySlip(bettingAccountId, slip);
        // Recalculate slip odds if you store total odds
        // (depends on your ReducerBetSlip model)
        // slip.recalculateTotalOdds();

        return slip;
    }


}
