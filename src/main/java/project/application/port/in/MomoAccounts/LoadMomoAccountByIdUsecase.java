package project.application.port.in.MomoAccounts;

import project.domain.model.MobileMoneyAccount;

public interface LoadMomoAccountByIdUsecase {
    MobileMoneyAccount loadMomoById(Long id);
}
