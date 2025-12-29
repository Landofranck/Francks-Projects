package project.application.service.betSlip;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.betSlip.AddEventPickToBetSlipUseCase;
import project.application.port.out.Match.ReadMatchByIdPort;
import project.application.port.out.bettingAccount.PersistEmptyBetSlipPort;
import project.application.port.out.bettingAccount.ReadEmptSlipByParenPort;
import project.domain.model.DraftBetSlip;
import project.domain.model.Match;
import project.domain.model.MatchEventPick;

@ApplicationScoped
public class AddEventPickToBetSlipUseCaseImpl implements AddEventPickToBetSlipUseCase {
    @Inject
    ReadMatchByIdPort readMatch;
    @Inject
    ReadEmptSlipByParenPort readEmptSlip;
    @Inject
    PersistEmptyBetSlipPort putBetSlip;

    @Override
    public DraftBetSlip addPick(Long bettingAccountId, Long matchId, String outcomeName) {
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
        if (matchId == null) {
            throw new IllegalArgumentException("matchId must not be null");
        }
        if (outcomeName == null || outcomeName.isBlank()) {
            throw new IllegalArgumentException("outcomeName must not be blank addevent impl");
        }

        Match match = readMatch.getMatch(matchId); // throws NotFound/IllegalArgument if not found

        // Find outcome in match
        var outcome = match.getMatchOutComes().stream()
                .filter(o -> outcomeName.equalsIgnoreCase(o.getOutcomeName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Outcome '" + outcomeName + "' not found for match " + matchId));
        //check if outcome is already in betslip
        boolean duplicate=slip.getPicks().stream()
                .anyMatch(o -> outcome.getOutcomeName().equalsIgnoreCase(o.getOutcomeName())&outcome.getMatchKey().equalsIgnoreCase(o.getMatchKey()));
        if(duplicate) throw new IllegalArgumentException("you have to choose another bick for this match addpickimpl 56");


        // Create pick from the outcome
        MatchEventPick pick = new MatchEventPick(
                outcome.getMatchKey(),
                outcome.getOutcomeName(),
                outcome.getOdd()
        );
        slip.addMatchEventPick(pick);
        putBetSlip.persistEmptyslip(bettingAccountId, slip);
        // Recalculate slip odds if you store total odds
        // (depends on your BetSlip model)
        // slip.recalculateTotalOdds();

        return slip;
    }
}
