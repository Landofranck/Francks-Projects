package project.application.port.out;

import project.domain.model.MatchOutComePick;

import java.util.List;

public interface UpdateMatchPort {
    void updateMatch(Long id, List<MatchOutComePick> in);
}
