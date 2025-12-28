package project.application.service.betSlip;

import jakarta.inject.Inject;
import project.application.port.in.betSlip.AddEventPickToBetSlipUseCase;
import project.application.port.out.Match.ReadMatchByIdPort;
import project.domain.model.BetSlip;
import project.domain.model.Match;
import project.domain.model.MatchEventPick;

public class AddEventPickToBetSlipUseCaseImpl implements AddEventPickToBetSlipUseCase {
    @Inject
    ReadMatchByIdPort readMatch;

    @Override
    public BetSlip addPick(Long bettingAccountId,BetSlip slip, Long matchId, String outcomeName) {
        if (slip.getParentAccount() == null || slip.getParentAccount().getAccountId() == null) {
            throw new IllegalArgumentException("BetSlip must have a parent betting account before adding picks");
        }
        if (!slip.getParentAccount().getAccountId().equals(bettingAccountId)) {
            throw new IllegalArgumentException("BetSlip does not belong to betting account " + bettingAccountId);
        }
        if (slip == null) {
            throw new IllegalArgumentException("bet slip must not be null");
        }
        if (matchId == null) {
            throw new IllegalArgumentException("matchId must not be null");
        }
        if (outcomeName == null || outcomeName.isBlank()) {
            throw new IllegalArgumentException("outcomeName must not be blank");
        }

        Match match = readMatch.getMatch(matchId); // throws NotFound/IllegalArgument if not found

        // Find outcome in match
        var outcome = match.getMatchOutComes().stream()
                .filter(o -> outcomeName.equalsIgnoreCase(o.getOutcomeName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Outcome '" + outcomeName + "' not found for match " + matchId));

        // Create pick from the outcome
        MatchEventPick pick = new MatchEventPick(
                outcome.getMatchKey(),
                outcome.getOutcomeName(),
                outcome.getOdd()
        );
        slip.addMatchEventPick(pick);

        // Recalculate slip odds if you store total odds
        // (depends on your BetSlip model)
        // slip.recalculateTotalOdds();

        return slip;
    }
}
