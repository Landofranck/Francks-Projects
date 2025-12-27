package project.application.port.in;

import project.domain.model.MobileMoneyAccount;

public interface CreateMobileMoneyAccountUseCase {
    Long createNewMobileMoneyAccount(MobileMoneyAccount account);
}

