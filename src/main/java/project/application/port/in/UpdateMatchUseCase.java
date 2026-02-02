package project.application.port.in;

import project.domain.model.Match;

public interface UpdateMatchUseCase {
    void updateMatch(Long MatchId, Match update);
}
