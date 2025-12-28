package project.application.service.betSlip;

import project.domain.model.BetSlip;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.betSlip.CreateEmptyBetSlipUseCase;
import project.application.port.out.ReadAccountByIdPort;
import project.domain.model.BetSlip;

@ApplicationScoped
public class CreateEmptyBetSlipUseCaseImpl implements CreateEmptyBetSlipUseCase {

    @Inject
    ReadAccountByIdPort readAccountByIdPort;

    @Override
    public BetSlip createEmpty(Long bettingAccountId, String category) {
        if (bettingAccountId == null) throw new IllegalArgumentException("bettingAccountId is required");
        if (category == null || category.isBlank()) throw new IllegalArgumentException("category is required");

        var account = readAccountByIdPort.getAccount(bettingAccountId); // ensures it exists
        var slip = new BetSlip(category);
        slip.setParentAccount(account); // ✅ parent set here
        return slip; // ✅ not saved
    }
}

