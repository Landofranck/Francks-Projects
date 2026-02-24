package project.application.port.in.betSlip;

public interface AddEventPickToBetSlipUseCase {
    void addPick(Long bettingAccountId, Long matchId, String outcomeName);
}
