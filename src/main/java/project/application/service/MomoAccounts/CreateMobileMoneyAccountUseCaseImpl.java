package project.application.service.MomoAccounts;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.MomoAccounts.CreateMobileMoneyAccountUseCase;
import project.application.port.out.mobilMoney.PersistMobileMoneyAccount;
import project.domain.model.MobileMoneyAccount;

import java.util.Objects;

@ApplicationScoped
public class CreateMobileMoneyAccountUseCaseImpl implements CreateMobileMoneyAccountUseCase {

    @Inject
    PersistMobileMoneyAccount persistMobileMoneyAccount;

    @Override
    public Long createNewMobileMoneyAccount(MobileMoneyAccount account) {
        Objects.requireNonNull(account, "account");
        return persistMobileMoneyAccount.saveMomoAccountToDataBase(account);
    }
}
