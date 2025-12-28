package project.application.port.in;

import java.math.BigDecimal;

public interface MakeWithdrawalUseCase {
    public void withdrawFromBettingToMobileMoney(Long bettingAccountId, Long momoAccountId, BigDecimal amount, String description);
}

