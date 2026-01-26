package project.application.port.in.MomoAccounts;

import java.math.BigDecimal;

public interface TopUpMomoUseCase {
    void topUp(Long momoId, BigDecimal amount, String Description);
}
