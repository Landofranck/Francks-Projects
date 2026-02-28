package project.application.service.MomoAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import project.application.port.in.MomoAccounts.TopUpMomoUseCase;
import project.application.port.out.mobilMoney.AppendMobileMoneyTransactionPort;
import project.application.port.out.mobilMoney.ReadMomoAccountByIdPort;
import project.application.port.out.mobilMoney.UpdateMobileMoneyBalancePort;
import project.config.TimeProvider;
import project.domain.model.Money;

import java.math.BigDecimal;
import java.time.Instant;

@ApplicationScoped
public class TopUpMomoUseCaseImpl implements TopUpMomoUseCase {

    @Inject
    ReadMomoAccountByIdPort readById;
    @Inject
    UpdateMobileMoneyBalancePort updateBalance;
    @Inject
    AppendMobileMoneyTransactionPort appendTx;
    @Inject
    TimeProvider timeProvider;

    @Transactional
    @Override
    public void topUp(Long momoId, Instant transactionTime, BigDecimal amount, String description) {
        var acc = readById.getMomoAccount(momoId);
        if (transactionTime == null) {
            transactionTime = Instant.now(timeProvider.clock());
        }
        var tx = acc.deposit(new Money(amount), transactionTime, description);

        updateBalance.updateBalance(acc);
        appendTx.appendToMobileMoney(momoId, tx);
    }
}
