package project.application.service.BettingAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.betSlip.UpdateSlipPickStatusUsecase;
import project.application.port.out.Match.FindMatchOutComeByParametersPort;
import project.application.port.out.Match.UpdateMatchPickStatusPort;
import project.application.port.out.Match.UpdateSlipPickStatusPort;
import project.application.port.out.bettingAccount.FindSlipEventOutComeByParamPort;
import project.domain.model.MatchOutComePick;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class UpdateSlipPickStatusUsecaseImpl implements UpdateSlipPickStatusUsecase {
    @Inject
    UpdateSlipPickStatusPort updateSlipPickStatus;
    @Inject
    UpdateMatchPickStatusPort updateMatchPickStatus;
    @Inject
    FindMatchOutComeByParametersPort findMatchOutComeByParameters;
    @Inject
    RefreshBettingStatusOfBetSlipsUseCaseImpl refreshBettingStatusOfBetSlips;
    @Inject
    FindSlipEventOutComeByParamPort findSlipEventOutComeByParamPort;

    @Override
    public void updateSlipPickStatus(MatchOutComePick pick) {
        var matchOutcomes = findMatchOutComeByParameters.findMatchOutComes(pick.getOwnerMatchName(), pick.getMatchKey(),pick.getOutcomeName(),pick.getLeague());
        var slipOutcomes=findSlipEventOutComeByParamPort.findSlipEventEntity(pick.getOwnerMatchName(), pick.getMatchKey(),pick.getOutcomeName(),pick.getLeague());
        Set<Long> accountIds = new HashSet<>();
        for (MatchOutComePick p : slipOutcomes) {
            accountIds.add(updateSlipPickStatus.updateSlipPick(p.getId(), pick.getOutcomePickStatus()));
        }
        for (MatchOutComePick p : matchOutcomes) {
            updateMatchPickStatus.updateMatchPick(p.getId(), pick.getOutcomePickStatus());
        }
        for (Long i: accountIds){
            refreshBettingStatusOfBetSlips.updateBettingAccountBets(i);
        }
    }
}
