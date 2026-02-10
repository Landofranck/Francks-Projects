package project.application.port.in.BettingAccount;

import project.domain.model.Enums.League;
import project.domain.model.MatchOutComePick;

import java.util.List;

public interface FindMatchOutComeByParametersUseCases {
    List<MatchOutComePick> findMatches(String matchkey, String outcomeName, League league);
}
