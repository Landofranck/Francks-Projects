package project.application.service.betSlip;

import project.application.port.out.bettingAccount.PersistEmptyBetSlipPort;
import project.domain.model.BetSlip;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.betSlip.CreateEmptyBetSlipUseCase;
import project.application.port.out.bettingAccount.ReadBettingAccountByIdPort;

@ApplicationScoped
public class CreateEmptyBetSlipUseCaseImpl implements CreateEmptyBetSlipUseCase {

    @Inject
    ReadBettingAccountByIdPort readAccountByIdPort;
    @Inject
    PersistEmptyBetSlipPort putEmptySlip;

    @Override
    public BetSlip createEmpty(Long bettingAccountId, String category) {
        if (bettingAccountId == null) throw new IllegalArgumentException("bettingAccountId is required");
        if (category == null || category.isBlank()) throw new IllegalArgumentException("category is required");

        var account = readAccountByIdPort.getBettingAccount(bettingAccountId); // ensures it exists
        var slip = new BetSlip(category);
        var betSlip = account.putEmptySlip(slip);// ✅ parent set here
        putEmptySlip.persisEmptyslip(account.getAccountId(), betSlip);
        return slip; // ✅ not saved
    }
}

