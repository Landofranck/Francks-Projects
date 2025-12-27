package project.adapter.in.web;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import project.adapter.in.web.TransactionDTO.TransactionDto;
import project.domain.model.Enums.AccountType;

import java.math.BigDecimal;
import java.util.List;

@XmlRootElement
public class MobileMoneyAccountDto implements AccountDto{
    private Long id;
    @NotNull(message = "the account must have a type")
    public  AccountType accountType;
    @NotNull(message = "the account must have a balance")
    private BigDecimal accountBalance;
    private Boolean dailyLimit;
    private Boolean weeklyLimit;
    private Boolean monthlyLimit;
    private List<TransactionDto> transactionHistory;

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setDailyLimit(Boolean dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public void setMonthlyLimit(Boolean monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public void setTransactionHistory(List<TransactionDto> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public void setWeeklyLimit(Boolean weeklyLimit) {
        this.weeklyLimit = weeklyLimit;
    }

    @Override
    public Long getId() {
        return this.id;
    }
}
