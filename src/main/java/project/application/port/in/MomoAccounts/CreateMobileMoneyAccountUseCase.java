package project.application.port.in.MomoAccounts;

import project.domain.model.MobileMoneyAccount;

public interface CreateMobileMoneyAccountUseCase {
    Long createNewMobileMoneyAccount(MobileMoneyAccount account);
}

