package project.application.service.BettingAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.betSlip.UpdateMatchPickStatusUsecase;
import project.application.port.out.Match.FindMatchOutComeByParametersPort;
import project.application.port.out.Match.UpdateMatchPickStatusPort;
import project.domain.model.MatchOutComePick;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class UpdateMatchPickStatusUsecaseImpl implements UpdateMatchPickStatusUsecase {
    @Inject
    UpdateMatchPickStatusPort updateMatchPickStatus;
    @Inject
    FindMatchOutComeByParametersPort findMatchOutComeByParameters;
    @Inject
    RefreshBettingStatusOfBetSlipsUseCaseImpl refreshBettingStatusOfBetSlips;

    @Override
    public void updateMatchPickStatus(MatchOutComePick pick) {
        var matchOutcomes = findMatchOutComeByParameters.findMatchOutComes(pick.getMatchKey(),pick.getOutcomeName(),pick.getLeague());
        Set<Long> accountIds = new HashSet<>();
        for (MatchOutComePick p : matchOutcomes) {
            accountIds.add(updateMatchPickStatus.updateMatchPick(p.getId(), pick.getOutcomePickStatus()));
        }
        for (Long i: accountIds){
            refreshBettingStatusOfBetSlips.updateBettingAccountBets(i);
        }
    }
}
