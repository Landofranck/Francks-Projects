package project.application.port.out.Match;

// ReadMatchByIdPort
public interface ReadMatchByIdPort {
    project.domain.model.Match getMatch(Long id);
}
