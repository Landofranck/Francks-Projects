package project.application.port.out.mobilMoney;

import project.domain.model.MobileMoneyAccount;

public interface ReadSummaryMomoAccountPort {
    MobileMoneyAccount getSummaryMomoAccount(Long id);
}
