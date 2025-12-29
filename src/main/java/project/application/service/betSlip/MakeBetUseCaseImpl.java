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
import project.domain.model.BetSlip;
import project.domain.model.Money;

import java.math.BigDecimal;
import java.time.Instant;

@ApplicationScoped
public class MakeBetUseCaseImpl implements MakeBetUseCase {

    @Inject
    ReadBettingAccountByIdPort readAccount;
    @Inject UpdateBettingAccountBalancePort updateBalance;
    @Inject AppendBettingAccountTransactionPort appendTx;
    @Inject PersistBetSlipToAccountPort persistSlip;
    @Inject TimeProvider timeProvider;

    @Transactional
    @Override
    public Long makeBet(Long bettingAccountId, BetSlip slip, BigDecimal stake, String description) {

        if (bettingAccountId == null) throw new IllegalArgumentException("bettingAccountId required");
        if (slip == null) throw new IllegalArgumentException("slip required");
        if (stake == null || stake.signum() <= 0) throw new IllegalArgumentException("stake must be > 0");
        if (slip.getPicks() == null || slip.getPicks().isEmpty())
            throw new IllegalArgumentException("betslip must contain picks");

        if (slip.getParentAccount() == null || slip.getParentAccount().getAccountId() == null)
            throw new IllegalArgumentException("BetSlip must have a parent account before making a bet");

        if (!slip.getParentAccount().getAccountId().equals(bettingAccountId))
            throw new IllegalArgumentException("BetSlip does not belong to betting account " + bettingAccountId);

        var account = readAccount.getBettingAccount(bettingAccountId);

        Instant now = Instant.now(timeProvider.clock());
        Money stakeMoney = new Money(stake);

        // Deduct stake + create transaction (your domain already has withdraw)
        var tx = account.withdraw(stakeMoney, now, description);

        // Put stake + timestamps on slip
        slip.setStake(stakeMoney);
        slip.setCreatedAt(now);

        // Persist account balance and tx
        updateBalance.updateBalance(account);
        appendTx.appendToBettingAccount(bettingAccountId, tx);

        // Persist betslip into betHistory
        Long slipId = persistSlip.persistSlipToAccount(bettingAccountId, slip);
        return slipId;
    }
}

