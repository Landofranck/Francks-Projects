package project.application.service.betSlip;

import project.adapter.in.web.Utils.Code;
import project.application.error.ConflictException;
import project.application.port.out.bettingAccount.PersistDraftBetSlipPort;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.betSlip.CreateEmptyBetSlipUseCase;
import project.application.port.out.bettingAccount.ReadBettingAccountByIdPort;
import project.domain.model.DraftBetSlip;
import project.domain.model.Enums.BetCategory;

import java.util.Map;

@ApplicationScoped
public class CreateEmptyBetSlipUseCaseImpl implements CreateEmptyBetSlipUseCase {

    @Inject
    ReadBettingAccountByIdPort readAccountByIdPort;
    @Inject
    PersistDraftBetSlipPort putEmptySlip;

    @Override
    public void createEmpty(Long bettingAccountId, BetCategory category) {

        if (bettingAccountId == null || bettingAccountId < 0)
            throw new ConflictException(Code.INVALID_INPUT,"bettingAccountId is required createmptslipimpl lin 24",Map.of("bettingId",bettingAccountId));
        if (category == null)
            throw new ConflictException(Code.INVALID_INPUT,"category is required createmptyslipimpl line 26", Map.of("bettingId",bettingAccountId));

        var account = readAccountByIdPort.getBettingAccount(bettingAccountId);// ensures it exists
        var slip = new DraftBetSlip(category);
        if (account.getDraftBetSlip()==null||account.getDraftBetSlip().getNumberOfEvents()>0) {
            account.putEmptySlip(slip);// ✅ parent set here
        }else {
            return;
        }
        putEmptySlip.persistDraftSlip(bettingAccountId, slip);//this is where the error seems to stem
// ✅ not saved

    }
}

