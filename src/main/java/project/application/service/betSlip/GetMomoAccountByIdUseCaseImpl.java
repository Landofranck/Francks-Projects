package project.application.service.betSlip;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.MomoAccounts.LoadMomoAccountByIdUsecase;
import project.application.port.out.mobilMoney.ReadMomoAccountByIdPort;
import project.domain.model.MobileMoneyAccount;

@ApplicationScoped
public class GetMomoAccountByIdUseCaseImpl implements LoadMomoAccountByIdUsecase {

    @Inject
    ReadMomoAccountByIdPort readMomoAccountById;
    @Override
    public MobileMoneyAccount loadMomoById(Long id) {
        return readMomoAccountById.getMomoAccount(id);
    }
}
