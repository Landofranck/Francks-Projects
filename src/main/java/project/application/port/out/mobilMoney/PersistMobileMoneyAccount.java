package project.application.port.out.mobilMoney;

import project.domain.model.MobileMoneyAccount;

public interface PersistMobileMoneyAccount {
    Long saveMomoAccountToDataBase(MobileMoneyAccount account);
}
