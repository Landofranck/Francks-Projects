package project.application.port.out.Match;

import project.domain.model.Enums.BetStatus;

public interface UpdateMatchPickStatusPort {
    Long updateMatchPick(Long MatchPickId, BetStatus newPickStatus);

}
