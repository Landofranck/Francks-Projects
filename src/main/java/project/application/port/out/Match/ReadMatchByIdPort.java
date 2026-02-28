package project.application.port.out.Match;

import project.domain.model.Match;

// ReadMatchByIdPort
public interface ReadMatchByIdPort {
    Match readMatch(Long id);
}
