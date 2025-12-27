package project.application.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import project.application.port.in.MakeDepositUseCase;
import project.application.port.out.*;
import project.domain.model.Enums.AccountType;
import project.domain.model.Enums.TransactionType;
import project.domain.model.Money;
import project.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@ApplicationScoped
public class MakeDepositUseCaseImpl implements MakeDepositUseCase {

    @Inject ReadMomoAccountByIdPort momoReader;
    @Inject ReadAccountByIdPort bettingReader;

    @Inject UpdateMobileMoneyBalancePort momoBalanceUpdater;
    @Inject UpdateBettingAccountBalancePort bettingBalanceUpdater;

    @Inject AppendMobileMoneyTransactionPort momoTxAppender;
    @Inject AppendBettingAccountTransactionPort bettingTxAppender;

    @Transactional
    @Override
    public void depositFromMobileMoneyToBettingAccount(Long momoAccountId, Long bettingAccountId, BigDecimal amount) {
        Objects.requireNonNull(momoAccountId, "momoAccountId");
        Objects.requireNonNull(bettingAccountId, "bettingAccountId");
        Objects.requireNonNull(amount, "amount");
        if (amount.signum() <= 0) throw new IllegalArgumentException("amount must be > 0");

        var momo = momoReader.getMomoAccount(momoAccountId);
        var betting = bettingReader.getAccount(bettingAccountId);

        Money transfer = new Money(amount);
        Money fee = calculateFee(momo.getAccountType(), transfer);

        Instant now = Instant.now();

        // --- STEP 1+2: take money + fee from momo (domain updates) ---
        momo.withdraw(transfer, now);
        if (!fee.isZero()) {
            momo.withdraw(fee, now);
        }

        // --- STEP 3: deposit to betting account ---
        betting.deposit(transfer); // ensure BettingAccount.deposit assigns back (Money immutable fix!)

        // --- STEP 4: append transactions (append-only) ---
        // You must append the SAME transactions the domain created OR recreate them consistently.
        // Here we recreate them consistently (same values and timestamp).
        momoTxAppender.appendToMobileMoney(momoAccountId,
                new Transaction(transfer, new Money(momo.getAccountBalance().getValue()), now, TransactionType.WITHDRAWAL));

        if (!fee.isZero()) {
            momoTxAppender.appendToMobileMoney(momoAccountId,
                    new Transaction(fee, new Money(momo.getAccountBalance().getValue()), now, TransactionType.WITHDRAWAL));
        }

        bettingTxAppender.appendToBettingAccount(bettingAccountId,
                new Transaction(transfer, new Money(betting.getBalance().getValue()), now, TransactionType.DEPOSIT));

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
