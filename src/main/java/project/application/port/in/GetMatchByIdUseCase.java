package project.application.port.in;

import project.domain.model.Match;

public interface GetMatchByIdUseCase {
    Match getMatchById(Long id);
}
