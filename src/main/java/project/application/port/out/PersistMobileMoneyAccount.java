package project.application.port.out;

import project.domain.model.MobileMoneyAccount;

public interface PersistMobileMoneyAccount {
    public long saveAccountToDataBase(MobileMoneyAccount account);
}
