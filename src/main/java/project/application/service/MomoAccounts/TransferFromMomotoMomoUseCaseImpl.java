package project.application.service.MomoAccounts;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import project.application.port.in.MomoAccounts.TransferMomoUseCase;
import project.application.port.out.mobilMoney.AppendMobileMoneyTransactionPort;
import project.application.port.out.mobilMoney.ReadMomoAccountByIdPort;
import project.application.port.out.mobilMoney.UpdateMobileMoneyBalancePort;
import project.config.TimeProvider;
import project.domain.model.Money;

import java.math.BigDecimal;
import java.time.Instant;

@ApplicationScoped
public class TransferFromMomotoMomoUseCaseImpl implements TransferMomoUseCase {

    @Inject ReadMomoAccountByIdPort readById;
    @Inject UpdateMobileMoneyBalancePort updateBalance;
    @Inject AppendMobileMoneyTransactionPort appendTx;
    @Inject
    TimeProvider timeProvider;

    @Transactional
    @Override
    public void transfer(Long fromId, Instant transactionTime,Long toId, BigDecimal amount, String description) {
        var from = readById.getMomoAccount(fromId);
        var to   = readById.getMomoAccount(toId);


        var money = new Money(amount);

        // withdrawal creates a WITHDRAWAL transaction in domain
        var outTx = from.withdraw(money, checkTime(transactionTime),description );

        // deposit creates a DEPOSIT transaction in domain
        var inTx = to.deposit(money,  checkTime(transactionTime),description);

        // persist balances
        updateBalance.updateBalance(from);
        updateBalance.updateBalance(to);

        // persist transactions
        appendTx.appendToMobileMoney(fromId, outTx);
        appendTx.appendToMobileMoney(toId, inTx);
    }
    private Instant checkTime(Instant instant){
        if(instant==null){
            return Instant.now(timeProvider.clock());
        }else {
            return instant;
        }
    }
}

