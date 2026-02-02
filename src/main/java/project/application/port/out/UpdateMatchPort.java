package project.application.port.out;

import project.domain.model.Match;

public interface UpdateMatchPort {
    void updateMatch(Long id, Match in);
}
