package project.application.port.in.betSlip;
import project.domain.model.BetSlip;
import project.domain.model.Money;

import java.math.BigDecimal;
import java.util.List;

public interface MakeBetUseCase {
    Long makeBet(Long bettingAccountId, List<Long> matchIds, List<String> matchOutComes, Money stake, String description);
}

