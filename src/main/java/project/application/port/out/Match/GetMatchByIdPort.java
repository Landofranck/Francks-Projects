package project.application.port.out.Match;

import project.domain.model.Enums.BrokerType;
import project.domain.model.Match;

public interface GetMatchByIdPort {
    Match getMatchById(Long id);
}
