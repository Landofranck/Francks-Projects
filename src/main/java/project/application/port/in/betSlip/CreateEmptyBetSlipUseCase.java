package project.application.port.in.betSlip;

public interface CreateEmptyBetSlipUseCase {
    project.domain.model.BetSlip createEmpty(String category);
}
