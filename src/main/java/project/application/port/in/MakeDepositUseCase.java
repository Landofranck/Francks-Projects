package project.application.port.in;

import java.math.BigDecimal;

public interface MakeDepositUseCase {
    public void depositFromMobileMoneyToBettingAccount(Long momoAccountId, Long bettingAccountId, BigDecimal amount);
}
