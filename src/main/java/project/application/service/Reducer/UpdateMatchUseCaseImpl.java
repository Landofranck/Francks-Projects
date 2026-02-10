package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.UpdateMatchUseCase;
import project.application.port.out.UpdateMatchPort;
import project.domain.model.MatchOutComePick;

import java.util.List;

@ApplicationScoped
public class UpdateMatchUseCaseImpl implements UpdateMatchUseCase {
    @Inject
    UpdateMatchPort updateMatch;

    @Override
    public void updateMatch(Long MatchId, List<MatchOutComePick> update) {
        updateMatch.updateMatch(MatchId, update);
    }
}
