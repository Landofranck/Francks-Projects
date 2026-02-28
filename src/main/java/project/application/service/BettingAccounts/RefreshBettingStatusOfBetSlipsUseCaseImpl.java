package project.application.service.BettingAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import project.application.port.in.BettingAccount.refreshBettingAccoutSlipsUseCase;
import project.application.port.out.bettingAccount.*;
import project.config.TimeProvider;
import project.domain.model.BetSlip;
import project.domain.model.Enums.BetStatus;
import project.domain.model.MatchOutComePick;

import java.time.Instant;
import java.util.Objects;

@ApplicationScoped
public class RefreshBettingStatusOfBetSlipsUseCaseImpl implements refreshBettingAccoutSlipsUseCase {
    @Inject
    ReadBettingAccountByIdPort getAccount;
    @Inject
    UpdateBettingAccountBalancePort updateBalance;
    @Inject
    AppendBettingAccountTransactionPort appendTx;
    @Inject
    TimeProvider timeProvider;
    @Inject
    UpdateBettingAccountPort updateBettingAccount;

    @Transactional
    @Override
    public void updateBettingAccountBets(Long bettingAccountId) {
        var account = getAccount.getBettingAccount(bettingAccountId);
        for (BetSlip b : account.getBetHistory()) {
            Instant now = null;
            BetStatus finalstatus = null;
            for (MatchOutComePick p : b.getPicks()) {
                if (now == null) now = p.getBegins();
                if (now.isBefore(p.getBegins())) now = p.getBegins();

                if (b.getStatus() != BetStatus.PENDING) continue;
                if (p.getOutcomePickStatus() == BetStatus.PENDING) {
                    finalstatus = BetStatus.PENDING;
                    break;
                }
                if (p.getOutcomePickStatus() == BetStatus.LOST) {
                    finalstatus = BetStatus.LOST;
                    break;
                }
                if (p.getOutcomePickStatus() == BetStatus.WON) {
                    finalstatus = BetStatus.PAID_OUT;
                }
            }

            Objects.requireNonNull(now.plusSeconds(7200), "match outcome pick time is null refreshbettingstatusimpl 54");
            b.setStatus(finalstatus);

            if (b.getStatus() == BetStatus.PAID_OUT) {
                var tx = account.betWonTransaction(b.getPotentialWinning(), now,b.getStrategy().toString(), b.getId());
                updateBalance.updateBalance(account);
                appendTx.appendToBettingAccount(bettingAccountId, tx);
                updateBettingAccount.updateBettingAccount(bettingAccountId, account);
            }
        }
    }
}
