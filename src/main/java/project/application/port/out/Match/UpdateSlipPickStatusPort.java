package project.application.port.out.Match;

import project.domain.model.Enums.BetStatus;

public interface UpdateSlipPickStatusPort {
    /**
     * returns a list of betting account id that have to be refreshed
     */
    Long updateSlipPick(Long MatchPickId, BetStatus newPickStatus);
}
