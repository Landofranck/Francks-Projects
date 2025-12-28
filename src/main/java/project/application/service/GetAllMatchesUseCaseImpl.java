package project.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.LoadAllMatchesUseCase;
import project.application.port.out.Match.ReadAllMatchesPort;
import project.domain.model.Match;

import java.util.List;

@ApplicationScoped
public class GetAllMatchesUseCaseImpl implements LoadAllMatchesUseCase {
    @Inject
    ReadAllMatchesPort readAll;
    public List<Match> getAllMatches() { return readAll.getAllMatches(); }
}
