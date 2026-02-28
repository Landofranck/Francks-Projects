package project.application.service.BettingAccounts;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import project.adapter.in.web.Utils.Code;
import project.application.error.ValidationException;
import project.application.port.in.MakeWithdrawalUseCase;
import project.application.port.out.bettingAccount.AppendBettingAccountTransactionPort;
import project.application.port.out.bettingAccount.ReadBettingAccountByIdPort;
import project.application.port.out.bettingAccount.UpdateBettingAccountBalancePort;
import project.application.port.out.mobilMoney.AppendMobileMoneyTransactionPort;
import project.application.port.out.mobilMoney.ReadMomoAccountByIdPort;
import project.application.port.out.mobilMoney.UpdateMobileMoneyBalancePort;
import project.domain.model.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@ApplicationScoped
public class MakeWithdrawalUseCaseImpl implements MakeWithdrawalUseCase {

    @Inject
    ReadBettingAccountByIdPort readBetting;
    @Inject
    ReadMomoAccountByIdPort readMomo;

    @Inject
    UpdateBettingAccountBalancePort updateBetting;
    @Inject
    UpdateMobileMoneyBalancePort updateMomo;

    @Inject
    AppendBettingAccountTransactionPort appendBettingTx;
    @Inject
    AppendMobileMoneyTransactionPort appendMomoTx;

    @Inject
    project.config.TimeProvider timeProvider;

    @Transactional
    @Override
    public void withdrawFromBettingToMobileMoney(Long bettingAccountId, Long momoAccountId, BigDecimal amount, Instant transactionTime,String description) {
        if (amount == null || amount.signum() <= 0) {
            throw new ValidationException(Code.INVALID_AMOUNT,"you have to enter a positve ammount withraw...imppl 46", Map.of("bettingId",bettingAccountId));
        }

        var betting = readBetting.getBettingAccount(bettingAccountId);
        var momo = readMomo.getMomoAccount(momoAccountId);

        var money = new Money(amount);

        // 1) withdraw from betting
        var bettingTx = betting.withdraw(money, description + ": to " + momo.getAccountId());

        // 2) deposit to momo
        var momoDepositTx = momo.deposit(money, checkTime(transactionTime), description + ": from " + betting.getAccountName());

        // 3) fee deducted from momo AFTER deposit
        BigDecimal fee = momo.getAccountType().name().equals("MTN")
                ? amount.multiply(new BigDecimal("0.01")).add(new BigDecimal("4"))
                : amount.multiply(new BigDecimal("0.01"));

// optional: round fee to 2 decimals if you want currency behavior:
        fee = fee.setScale(2, java.math.RoundingMode.HALF_UP);
        var feeTx = momo.withdraw(new Money(fee), checkTime(transactionTime), description + ": to " + betting.getAccountName());

        // persist balances
        updateBetting.updateBalance(betting);
        updateMomo.updateBalance(momo);

        // persist transactions
        appendBettingTx.appendToBettingAccount(bettingAccountId, bettingTx);
        appendMomoTx.appendToMobileMoney(momoAccountId, momoDepositTx);
        appendMomoTx.appendToMobileMoney(momoAccountId, feeTx);
    }
    private Instant checkTime(Instant time){
        if (time==null){
            return Instant.now(timeProvider.clock());
        }else {
            return time;
        }
    }
}

