package project.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.betSlip.CreateMatchUseCase;
import project.application.port.out.Match.PersistMatchPort;
import project.domain.model.Match;

@ApplicationScoped
public class CreateMatchUseCaseImpl implements CreateMatchUseCase {
    @Inject
    PersistMatchPort persist;

    public Long createMatch(Match match) {
        return persist.saveMatch(match);
    }
}
