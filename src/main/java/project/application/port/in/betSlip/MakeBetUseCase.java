package project.application.port.in.betSlip;

public interface MakeBetUseCase {
    project.domain.model.BetSlip addBet(Long bettingAccountId, project.domain.model.BetSlip slip);
}
