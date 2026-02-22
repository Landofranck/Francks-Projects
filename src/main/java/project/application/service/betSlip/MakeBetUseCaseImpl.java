package project.application.service.betSlip;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import project.application.port.in.betSlip.MakeBetUseCase;
import project.application.port.out.bettingAccount.*;
import project.config.TimeProvider;
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
    @Inject
    UpdateBettingAccountPort updateBettingAccount;

    @Transactional
    @Override
    public Long makeBet(Long bettingAccountId, List<Long> matchIds, List<String> matchOutcomes, Money stake, BetStrategy strategy, Integer bonusSlip) {
        var isBonus = bonusSlip != null && bonusSlip >= 0;
        var test = (bonusSlip == null || bonusSlip < 0);
        if (bettingAccountId == null) throw new IllegalArgumentException("bettingAccountId required");
        if (stake == null || stake.getValue().signum() <= 0 && test)
            throw new IllegalArgumentException("stake must be > 0, or you choose a bonus");
        if (matchIds != null) {

            removeAllPicks.removeAllPicks(bettingAccountId);
            int i = 0;
            for (Long id : matchIds) {
                addPicks.addPick(bettingAccountId, id, matchOutcomes.get(i));
                i++;
            }
        }

        var account = readAccount.getBettingAccount(bettingAccountId);
        Instant now = Instant.now(timeProvider.clock());

        var draftSlip = account.getDraftBetSlip();
        var out=persistSlip.persistSlipToAccount(bettingAccountId, draftSlip, strategy);
        var tx = account.placeBetTransaction(stake, strategy.toString(),out);
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
        updateBettingAccount.updateBettingAccount(bettingAccountId, account);

        return out;

    }
}

