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
import project.domain.model.Enums.BetStrategy;
import project.domain.model.Money;

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
    public Long makeBet(Long bettingAccountId, List<Long> matchIds, List<String> matchOutcomes, Money stake, BetStrategy strategy, Integer bonusSlip) {
        var isBonus = bonusSlip != null && bonusSlip >= 0;
        var test=(bonusSlip==null||bonusSlip<0);
        if (bettingAccountId == null) throw new IllegalArgumentException("bettingAccountId required");
        if (stake == null || stake.getValue().signum() <= 0 &&test) throw new IllegalArgumentException("stake must be > 0, or you choose a bonus");
        if (matchIds != null) {

            removeAllPicks.removeAllPicks(bettingAccountId);
            int i = 0;
            for (Long Ids : matchIds) {
                addPicks.addPick(bettingAccountId, matchIds.get(i), matchOutcomes.get(i));
                i++;
            }
        }

        var account = readAccount.getBettingAccount(bettingAccountId);
        Instant now = Instant.now(timeProvider.clock());


        var tx = account.placeBetTransaction(stake, strategy.toString());
        var draftSlip = account.getDraftBetSlip();

        if (isBonus) {
            account.placeBonusBet(bonusSlip, now);

        } else {
            account.placeBet(stake, now);

            // Persist account balance and tx
            updateBalance.updateBalance(account);
            appendTx.appendToBettingAccount(bettingAccountId, tx);
        }
        draftSlip.checkCategory();
        // Persist betslip into betHistory
        return persistSlip.persistSlipToAccount(bettingAccountId, draftSlip, strategy);

    }
}

