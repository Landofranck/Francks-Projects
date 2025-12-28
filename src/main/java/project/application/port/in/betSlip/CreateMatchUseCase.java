package project.application.port.in.betSlip;

import project.domain.model.Match;

public interface CreateMatchUseCase {
    Long createMatch(Match match);
}

