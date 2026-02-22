package project.application.service.MomoAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.MomoAccounts.LoadMomoAccountByIdUsecase;
import project.application.port.out.mobilMoney.ReadMomoAccountByIdPort;
import project.application.port.out.mobilMoney.ReadSummaryMomoAccountPort;
import project.domain.model.MobileMoneyAccount;

@ApplicationScoped
public class GetMomoAccountByIdUseCaseImpl implements LoadMomoAccountByIdUsecase {

    @Inject
    ReadSummaryMomoAccountPort readMomoAccountById;
    @Override
    public MobileMoneyAccount loadMomoById(Long id) {
        return readMomoAccountById.getSummaryMomoAccount(id);
    }
}
