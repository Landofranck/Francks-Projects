package project.application.port.in.BettingAccount;

import project.domain.model.Enums.League;
import project.domain.model.Enums.MatchKey;
import project.domain.model.MatchOutComePick;

import java.util.List;

public interface FindMatchOutComeByParametersUseCases {
    List<MatchOutComePick> findMatches(String ownerName,MatchKey matchkey, String outcomeName, League league);
}
