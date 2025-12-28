package project.application.port.out.mobilMoney;

import project.domain.model.Transaction;

public interface AppendMobileMoneyTransactionPort {
    void appendToMobileMoney(Long momoAccountId, Transaction transaction);
}
