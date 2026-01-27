package project.application.port.out;

import project.domain.model.Match;

public interface LoadMatchByIdUseCase {
    Match loadMatch(Long id);
}
