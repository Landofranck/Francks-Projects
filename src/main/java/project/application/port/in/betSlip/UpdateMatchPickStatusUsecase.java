package project.application.port.in.betSlip;

import jakarta.inject.Inject;
import project.domain.model.MatchOutComePick;

public interface UpdateMatchPickStatusUsecase {
   void updateMatchPickStatus(MatchOutComePick pick);
}
