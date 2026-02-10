package project.application.port.out.Match;

import project.domain.model.Enums.League;
import project.domain.model.MatchOutComePick;

import java.util.List;

public interface FindMatchOutComeByParametersPort {
    List<MatchOutComePick> findMatchOutComes(String matchKey, String outComeName, League league);
}
