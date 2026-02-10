package project.application.port.out.Match;

import project.domain.model.Enums.BetStatus;

public interface UpdateMatchPickStatusPort {
    /**
     * returns a list of betting account id that have to be refreshed
     */
    Long updateMatchPick(Long MatchPickId, BetStatus newPickStatus);
}
