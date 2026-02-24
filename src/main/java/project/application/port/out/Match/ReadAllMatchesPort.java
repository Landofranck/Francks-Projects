package project.application.port.out.Match;

import project.domain.model.Enums.BrokerType;
import project.domain.model.Match;

import java.time.Instant;
import java.util.List;

// ReadAllMatchesPort
public interface ReadAllMatchesPort {
    List<Match> getAllMatches(BrokerType broker, Long id, String name, Instant start, Instant stop);
}
