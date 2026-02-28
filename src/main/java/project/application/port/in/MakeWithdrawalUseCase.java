package project.application.port.in;

import java.math.BigDecimal;
import java.time.Instant;

public interface MakeWithdrawalUseCase {
    public void withdrawFromBettingToMobileMoney(Long bettingAccountId, Long momoAccountId,  BigDecimal amount,Instant transactionTime, String description);
}

