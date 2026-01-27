package project.application.service.BettingAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.out.LoadMatchByIdUseCase;
import project.application.port.out.Match.ReadMatchByIdPort;
import project.domain.model.Match;
@ApplicationScoped
public class GetMatchByIdUseCaseImpl implements LoadMatchByIdUseCase {
    @Inject
    ReadMatchByIdPort readMatch;
    @Override
    public Match loadMatch(Long id) {
        return readMatch.getMatch(id);
    }
}
