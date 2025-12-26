package project.application.port.out;

import project.domain.model.MobileMoneyAccount;

import java.util.List;

public interface ReadAllMomoAccounts {
    public List<MobileMoneyAccount> getAllMomoAccounts();
}
