package project.application.port.in.BettingAccount;

import project.domain.model.Match;

public interface LoadMatchByIdUseCase {
    Match loadMatch(Long id);
}
