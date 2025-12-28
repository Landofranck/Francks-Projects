package project.application.port.out.bettingAccount;

import project.domain.model.Transaction;

public interface AppendBettingAccountTransactionPort {
    void appendToBettingAccount(Long bettingAccountId, Transaction transaction);
}
