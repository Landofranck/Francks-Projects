package project.application.service.betSlip;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.betSlip.RemoveAllPicksFromDraftSlipUseCase;
import project.application.port.out.bettingAccount.PersistEmptyBetSlipPort;
import project.application.port.out.bettingAccount.ReadEmptSlipByParenPort;
import project.domain.model.DraftBetSlip;

@ApplicationScoped
public class RemoveAllPicksImpl implements RemoveAllPicksFromDraftSlipUseCase {
    @Inject
    ReadEmptSlipByParenPort readEmptSlip;
    @Inject
    PersistEmptyBetSlipPort putBetSlip;

    @Override
    public DraftBetSlip removeAllPicks(Long bettingId) {
        var slip = readEmptSlip.getAvailableBettingSlip(bettingId);
        if (slip == null) {
            throw new IllegalArgumentException("no betslip found 27");
        }

        if (slip.getDraftSlipOwner() == null || slip.getDraftSlipOwner().getAccountId() == null) {
            throw new IllegalArgumentException("ReducerBetSlip must have a parent betting account before adding picks addpickimpl 27");
        }
        if (!slip.getDraftSlipOwner().getAccountId().equals(bettingId)) {
            throw new IllegalArgumentException("ReducerBetSlip does not belong to betting account " + bettingId);
        }
        if (slip == null) {
            throw new IllegalArgumentException("bet slip must not be null");
        }

        slip.removeAllMatchEventPicks();
        putBetSlip.persistEmptyslip(bettingId, slip);
        // Recalculate slip odds if you store total odds
        // (depends on your ReducerBetSlip model)
        // slip.recalculateTotalOdds();

        return slip;
    }


}
