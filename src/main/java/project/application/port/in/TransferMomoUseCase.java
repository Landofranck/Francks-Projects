package project.application.port.in;


import java.math.BigDecimal;

public interface TransferMomoUseCase {
    void transfer(Long fromId, Long toId, BigDecimal amount, String description);
}

