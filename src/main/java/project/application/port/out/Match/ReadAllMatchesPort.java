package project.application.port.out.Match;

import project.domain.model.Enums.BrokerType;

import java.time.Instant;

// ReadAllMatchesPort
public interface ReadAllMatchesPort {
    java.util.List<project.domain.model.Match> getAllMatches(BrokerType broker, Long id,String name, Instant start, Instant stop);
}
