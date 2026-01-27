package project.application.service.betSlip;

import project.application.port.out.bettingAccount.PersistEmptyBetSlipPort;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.betSlip.CreateEmptyBetSlipUseCase;
import project.application.port.out.bettingAccount.ReadBettingAccountByIdPort;
import project.domain.model.DraftBetSlip;
import project.domain.model.Enums.BetCategory;

@ApplicationScoped
public class CreateEmptyBetSlipUseCaseImpl implements CreateEmptyBetSlipUseCase {

    @Inject
    ReadBettingAccountByIdPort readAccountByIdPort;
    @Inject
    PersistEmptyBetSlipPort putEmptySlip;

    @Override
    public DraftBetSlip createEmpty(Long bettingAccountId, BetCategory category) {

        if (bettingAccountId == null || bettingAccountId < 0)
            throw new IllegalArgumentException("bettingAccountId is required createmptslipimpl lin 24");
        if (category == null || category==null)
            throw new IllegalArgumentException("category is required createmptslipimpl line 26");

        var account = readAccountByIdPort.getBettingAccount(bettingAccountId);// ensures it exists
        var slip = new DraftBetSlip(category);
        var betSlip = account.putEmptySlip(slip);// ✅ parent set here

        DraftBetSlip draftSlip = putEmptySlip.persistEmptyslip(bettingAccountId, slip);//this is where the error seems to stem

        return draftSlip; // ✅ not saved

    }
}

