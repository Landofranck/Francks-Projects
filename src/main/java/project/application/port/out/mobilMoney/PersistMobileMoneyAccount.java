package project.application.port.out.mobilMoney;

import project.domain.model.MobileMoneyAccount;

public interface PersistMobileMoneyAccount {
    public Long saveMomoAccountToDataBase(MobileMoneyAccount account);
}
