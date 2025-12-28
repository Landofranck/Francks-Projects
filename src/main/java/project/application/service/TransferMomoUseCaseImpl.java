package project.application.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import project.application.port.in.TransferMomoUseCase;
import project.application.port.out.AppendMobileMoneyTransactionPort;
import project.application.port.out.ReadMomoAccountByIdPort;
import project.application.port.out.UpdateMobileMoneyBalancePort;
import project.domain.model.Money;

import java.math.BigDecimal;
import java.time.Instant;

@ApplicationScoped
public class TransferMomoUseCaseImpl implements TransferMomoUseCase {

    @Inject ReadMomoAccountByIdPort readById;
    @Inject UpdateMobileMoneyBalancePort updateBalance;
    @Inject AppendMobileMoneyTransactionPort appendTx;

    @Transactional
    @Override
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        var from = readById.getMomoAccount(fromId);
        var to   = readById.getMomoAccount(toId);

        var money = new Money(amount);
        var now = Instant.now();

        // withdrawal creates a WITHDRAWAL transaction in domain
        var outTx = from.withdraw(money, now);

        // deposit creates a DEPOSIT transaction in domain
        var inTx = to.deposit(money, now);

        // persist balances
        updateBalance.updateBalance(from);
        updateBalance.updateBalance(to);

        // persist transactions
        appendTx.appendToMobileMoney(fromId, outTx);
        appendTx.appendToMobileMoney(toId, inTx);
    }
}

