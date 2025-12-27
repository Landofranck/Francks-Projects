package project.application.port.out;

import project.domain.model.MobileMoneyAccount;

public interface ReadMomoAccountByIdPort {
    MobileMoneyAccount getMomoAccount(Long id);
}
