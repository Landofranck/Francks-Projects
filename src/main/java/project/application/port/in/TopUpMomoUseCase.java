package project.application.port.in;

import java.math.BigDecimal;

public interface TopUpMomoUseCase {
    void topUp(Long momoId, BigDecimal amount, String Description);
}
