package project.application.port.in.MomoAccounts;

import java.math.BigDecimal;
import java.time.Instant;

public interface TopUpMomoUseCase {
    void topUp(Long momoId, Instant transactionTime, BigDecimal amount, String Description);
}
