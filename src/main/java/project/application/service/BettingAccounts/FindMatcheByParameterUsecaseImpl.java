package project.application.service.BettingAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.BettingAccount.FindMatchOutComeByParametersUseCases;
import project.application.port.out.Match.FindMatchOutComeByParametersPort;
import project.domain.model.Enums.League;
import project.domain.model.MatchOutComePick;

import java.util.List;

@ApplicationScoped
public class FindMatcheByParameterUsecaseImpl implements FindMatchOutComeByParametersUseCases {
    @Inject
    FindMatchOutComeByParametersPort findMatchOutComeByParametersPort;
    @Override
    public List<MatchOutComePick> findMatches(String matchKey, String outComeName, League league) {
        return findMatchOutComeByParametersPort.findMatchOutComes(matchKey,outComeName,league);
    }
}
