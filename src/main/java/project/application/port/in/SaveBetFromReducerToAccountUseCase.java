package project.application.port.in;

import project.domain.model.BetSlip;

public interface SaveBetFromReducerToAccountUseCase {
    public void saveBetslip(Long parentAccountId, BetSlip slipToBeSaved);
}
