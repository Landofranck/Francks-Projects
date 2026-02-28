package project.application.port.in.MomoAccounts;

import java.math.BigDecimal;
import java.time.Instant;

public interface MakeDepositUseCase {
    void depositFromMobileMoneyToBettingAccount(Long momoAccountId, Instant time, Long bettingAccountId, BigDecimal amount, String description);
}
