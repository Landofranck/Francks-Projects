package project.application.port.in;

import project.domain.model.Enums.BrokerType;
import project.domain.model.Match;

import java.time.Instant;
import java.util.List;

public interface LoadAllMatchesUseCase {
   List<Match> getAllMatches(BrokerType broker, Long id,String name, Instant start, Instant ende);
}
