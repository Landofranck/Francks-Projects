package project.application.port.in.MomoAccounts;


import java.math.BigDecimal;
import java.time.Instant;

public interface TransferMomoUseCase {
    void transfer(Long fromId, Instant transactionTime, Long toId, BigDecimal amount, String description);
}

