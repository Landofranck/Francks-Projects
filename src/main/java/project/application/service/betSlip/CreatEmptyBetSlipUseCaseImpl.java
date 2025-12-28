package project.application.service.betSlip;

import project.domain.model.BetSlip;

public class CreatEmptyBetSlipUseCaseImpl {
    public BetSlip createEmpty(String category) {
        return new BetSlip(category); // or however your BetSlip constructor works
    }
}
