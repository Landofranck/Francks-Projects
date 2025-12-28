package project.application.port.in.betSlip;
import project.domain.model.BetSlip;
import java.math.BigDecimal;

public interface MakeBetUseCase {
    Long makeBet(Long bettingAccountId, BetSlip slip, BigDecimal stake, String description);
}

