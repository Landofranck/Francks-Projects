package project.application.port.out.mobilMoney;

import project.domain.model.MobileMoneyAccount;

public interface UpdateMobileMoneyBalancePort {
    void updateBalance(MobileMoneyAccount account);
}
