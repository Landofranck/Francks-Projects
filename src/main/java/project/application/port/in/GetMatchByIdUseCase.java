package project.application.port.in;

import project.domain.model.Enums.BrokerType;
import project.domain.model.Match;

public interface GetMatchByIdUseCase {
    Match getMatchByIdOrName(Long id, BrokerType broker, String name);
}
