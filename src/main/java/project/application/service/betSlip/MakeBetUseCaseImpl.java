package project.application.service.betSlip;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import project.application.port.in.betSlip.MakeBetUseCase;
import project.application.port.out.bettingAccount.ReadBettingAccountByIdPort;
import project.application.port.out.bettingAccount.AppendBettingAccountTransactionPort;
import project.application.port.out.bettingAccount.PersistBetSlipToAccountPort;
import project.application.port.out.bettingAccount.UpdateBettingAccountBalancePort;
import project.config.TimeProvider;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class MakeBetUseCaseImpl implements MakeBetUseCase {

    @Inject
    ReadBettingAccountByIdPort readAccount;
    @Inject
    UpdateBettingAccountBalancePort updateBalance;
    @Inject
    AppendBettingAccountTransactionPort appendTx;
    @Inject
    PersistBetSlipToAccountPort persistSlip;
    @Inject
    AddEventPickToBetSlipUseCaseImpl addPicks;
    @Inject
    RemoveAllPicksImpl removeAllPicks;
    @Inject
    TimeProvider timeProvider;

    @Transactional
    @Override
    public Long makeBet(Long bettingAccountId, List<Long> matchIds, List<String> matchOutcomes, BigDecimal stake, String description) {

        if (bettingAccountId == null) throw new IllegalArgumentException("bettingAccountId required");
        if (stake == null || stake.signum() <= 0) throw new IllegalArgumentException("stake must be > 0");
        if (matchIds != null) {
            removeAllPicks.removeAllPicks(bettingAccountId);
            int i=0;
            for (Long Ids:matchIds) {
                addPicks.addPick(bettingAccountId, matchIds.get(i), matchOutcomes.get(i));
                i++;
            }
        }

        var account = readAccount.getBettingAccount(bettingAccountId);

        Instant now = Instant.now(timeProvider.clock());
        Money stakeMoney = new Money(stake);


        var tx = account.placeBet(stakeMoney, now, description);
        var draftSlip = account.getDraftBetSlip();
        // Put stake + timestamps on slip
        draftSlip.setStake(stakeMoney);
        draftSlip.setCreatedAt(now);
        draftSlip.setStatus(BetStatus.PENDING);

        // Persist account balance and tx
        updateBalance.updateBalance(account);
        appendTx.appendToBettingAccount(bettingAccountId, tx);

        // Persist betslip into betHistory
        Long slipId = persistSlip.persistSlipToAccount(bettingAccountId, draftSlip, description);
        return slipId;
    }
}

