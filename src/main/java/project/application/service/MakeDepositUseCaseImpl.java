package project.application.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import project.application.port.in.MakeDepositUseCase;
import project.application.port.out.*;
import project.config.TimeProvider;
import project.domain.model.Enums.AccountType;
import project.domain.model.Money;
import project.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@ApplicationScoped
public class MakeDepositUseCaseImpl implements MakeDepositUseCase {
    @Inject
    TimeProvider timeProvider;

    @Inject
    ReadMomoAccountByIdPort momoReader;
    @Inject
    ReadAccountByIdPort bettingReader;

    @Inject
    UpdateMobileMoneyBalancePort momoBalanceUpdater;
    @Inject
    UpdateBettingAccountBalancePort bettingBalanceUpdater;

    @Inject
    AppendMobileMoneyTransactionPort momoTxAppender;
    @Inject
    AppendBettingAccountTransactionPort bettingTxAppender;

    @Transactional
    @Override
    public void depositFromMobileMoneyToBettingAccount(Long momoAccountId, Long bettingAccountId, BigDecimal amount, String description) {
        Objects.requireNonNull(momoAccountId, "momoAccountId");
        Objects.requireNonNull(bettingAccountId, "bettingAccountId");
        Objects.requireNonNull(amount, "amount");
        if (amount.signum() <= 0) throw new IllegalArgumentException("amount must be > 0");
        var momo = momoReader.getMomoAccount(momoAccountId);
        var betting = bettingReader.getAccount(bettingAccountId);

        Money transfer = new Money(amount);
        Money fee = calculateFee(momo.getAccountType(), transfer);

        var now = Instant.now(timeProvider.clock());

        // 1) withdraw transfer from momo -> DOMAIN creates tx
        Transaction momoTransferTx = momo.withdraw(transfer, now,description );


        // 1) withdraw transfer from momo -> DOMAIN creates tx
        // 2) withdraw fee from momo -> DOMAIN creates tx
        Transaction momoFeeTx = fee.isZero() ? null : momo.withdraw(fee, now, description+" transaction fee");


        // 3) deposit transfer into betting -> DOMAIN creates tx
        Transaction bettingDepositTx = betting.deposit(transfer, now,description); // ensure BettingAccount.deposit assigns back (Money immutable fix!)

        // --- STEP 4: append transactions (append-only) ---
        // You must append the SAME transactions the domain created OR recreate them consistently.
        // Here we recreate them consistently (same values and timestamp).
        momoTxAppender.appendToMobileMoney(momoAccountId, momoTransferTx);

        if (!fee.isZero()) {
            momoTxAppender.appendToMobileMoney(momoAccountId, momoFeeTx);
        }

        bettingTxAppender.appendToBettingAccount(bettingAccountId, bettingDepositTx);

        // --- Persist balances only (no history overwrite) ---
        momoBalanceUpdater.updateBalance(momo);
        bettingBalanceUpdater.updateBalance(betting);
    }

    private Money calculateFee(AccountType type, Money transferAmount) {
        BigDecimal base = transferAmount.getValue();
        BigDecimal onePercent = base.multiply(BigDecimal.valueOf(0.01));

        return switch (type) {
            case MTN -> new Money(onePercent.add(BigDecimal.valueOf(4)));
            case ORANGE -> new Money(onePercent);
            default -> throw new IllegalStateException("Unsupported momo type: " + type);
        };
    }
}
