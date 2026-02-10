package project.application.port.in;

import project.domain.model.Match;
import project.domain.model.MatchOutComePick;

import java.util.List;

public interface UpdateMatchUseCase {
    void updateMatch(Long MatchId, List<MatchOutComePick> update);
}
