package project.adapter.in.web.BettinAccountDTO;


import project.domain.model.Enums.AccountType;
import java.math.BigDecimal;
import java.util.List;

public class BettingAccountDto {
    private Long id;
    private String accountName;
    private AccountType brokerType;
    private BigDecimal balance;
    private List<TransactionDto> transactionHistory;
    private List<BetSlipDto> betHistory;

    public BettingAccountDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public AccountType getBrokerType() { return brokerType; }
    public void setBrokerType(AccountType brokerType) { this.brokerType = brokerType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public List<TransactionDto> getTransactionHistory() { return transactionHistory; }
    public void setTransactionHistory(List<TransactionDto> transactionHistory) { this.transactionHistory = transactionHistory; }

    public List<BetSlipDto> getBetHistory() { return betHistory; }
    public void setBetHistory(List<BetSlipDto> betHistory) { this.betHistory = betHistory; }
}
