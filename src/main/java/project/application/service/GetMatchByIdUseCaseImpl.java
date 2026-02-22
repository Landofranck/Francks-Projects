package project.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.GetMatchByIdUseCase;
import project.application.port.out.Match.GetMatchByIdPort;
import project.domain.model.Enums.BrokerType;
import project.domain.model.Match;

import java.util.List;

@ApplicationScoped
public class GetMatchByIdUseCaseImpl implements GetMatchByIdUseCase {
    @Inject
    GetMatchByIdPort getMatchByIdPort;
    @Override
    public Match getMatchByIdOrName(Long id, BrokerType broker, String name) {
        return getMatchByIdPort.getMatchByIdOrName(id, broker ,name);
    }
}
