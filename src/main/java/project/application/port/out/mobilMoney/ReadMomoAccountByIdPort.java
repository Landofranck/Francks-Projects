package project.application.port.out.mobilMoney;

import project.domain.model.MobileMoneyAccount;

public interface ReadMomoAccountByIdPort {
    MobileMoneyAccount getMomoAccount(Long id);
}
