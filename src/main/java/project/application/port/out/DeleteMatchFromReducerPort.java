package project.application.port.out;

public interface DeleteMatchFromReducerPort {
    void deleteMatch(Long reducerId, Long matchId);
}
