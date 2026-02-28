package project.application.service.betSlip;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.adapter.in.web.Utils.Code;
import project.application.error.ValidationException;
import project.application.port.in.betSlip.AddEventPickToBetSlipUseCase;
import project.application.port.out.Match.ReadMatchByIdPort;
import project.application.port.out.bettingAccount.PersistDraftBetSlipPort;
import project.application.port.out.bettingAccount.ReadEmptSlipByParenPort;
import project.domain.model.Match;
import project.domain.model.MatchOutComePick;

import java.util.Map;

@ApplicationScoped
public class AddEventPickToBetSlipUseCaseImpl implements AddEventPickToBetSlipUseCase {
    @Inject
    ReadMatchByIdPort readMatch;
    @Inject
    ReadEmptSlipByParenPort readEmptSlip;
    @Inject
    PersistDraftBetSlipPort putBetSlip;

    @Override
    public void addPick(Long bettingAccountId, Long matchId, String outcomeName) {
        var slip = readEmptSlip.getAvailableBettingSlip(bettingAccountId);
        if (slip == null) {
            throw new IllegalArgumentException("no betslip found 27");
        }

        if (slip.getDraftSlipOwner() == null || slip.getDraftSlipOwner().getAccountId() == null) {
            throw new IllegalArgumentException("ReducerBetSlip must have a parent betting account before adding picks addpickimpl 27");
        }
        if (!slip.getDraftSlipOwner().getAccountId().equals(bettingAccountId)) {
            throw new IllegalArgumentException("ReducerBetSlip does not belong to betting account " + bettingAccountId);
        }
        if (matchId == null) {
            throw new IllegalArgumentException("matchIds must not be null AddEventPickToBetSlipUseCaseImpl 36");
        }
        if (outcomeName == null || outcomeName.isBlank()) {
            throw new IllegalArgumentException("outcomeName must not be blank addevent impl");
        }

        Match match = readMatch.readMatch(matchId); // throws NotFound/IllegalArgument if not found

        // Find outcome in match
        var outcome = match.getMatchOutComes().stream()
                .filter(o -> outcomeName.equalsIgnoreCase(o.getOutcomeName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Outcome '" + outcomeName + "' not found for match " + matchId));
        //check if outcome is already in betslip
        boolean duplicate=slip.getPicks().stream()
                .anyMatch(o -> outcome.getOwnerMatchName().equalsIgnoreCase(o.getOwnerMatchName()));
        if(duplicate) throw new ValidationException(Code.SLIP_ERROR,"you have to choose another pick for this match addpickimpl 56", Map.of("bettingId",bettingAccountId));


        // Create pick from the outcome
        MatchOutComePick pick = new MatchOutComePick(
                outcome.getIdentity(),
                outcome.getMatchKey(),
                outcome.getOutcomeName(),
                outcome.getOdd(),
                outcome.getLeague()
        );
        pick.setOutcomePickStatus(outcome.getOutcomePickStatus());
        pick.setOwnerMatchName(outcome.getOwnerMatchName());
        pick.setBegins(outcome.getBegins());
        slip.addMatchEventPick(pick);

        putBetSlip.persistDraftSlip(bettingAccountId, slip);
        // Recalculate slip odds if you store total odds
        // (depends on your ReducerBetSlip model)
        // slip.recalculateTotalOdds();
    }
}
