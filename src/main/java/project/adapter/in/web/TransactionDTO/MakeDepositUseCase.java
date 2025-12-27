package project.adapter.in.web.TransactionDTO;


import java.math.BigDecimal;

public interface MakeDepositUseCase {
    void depositFromMobileMoneyToBettingAccount(Long momoAccountId, Long bettingAccountId, BigDecimal amount);
}

