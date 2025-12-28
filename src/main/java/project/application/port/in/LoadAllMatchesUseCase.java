package project.application.port.in;

import project.domain.model.Match;

import java.util.List;

public interface LoadAllMatchesUseCase {
    java.util.List<Match> getAllMatches();
}
