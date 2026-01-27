package project.adapter.in.web.bettinAccountDTO;


import project.adapter.in.web.TransactionDTO.TransactionDto;
import project.adapter.in.web.bettinAccountDTO.betslip.BetSlipDto;
import project.domain.model.Enums.BrokerType;

import java.math.BigDecimal;
import java.util.List;

public class BettingAccountDto {
    private Long id;
    private String accountName;
    private BrokerType brokerType;
    private BigDecimal balance;
    private List<TransactionDto> transactionHistory;
    private List<BetSlipDto> betHistory;
    private BetSlipDto draftAccount;


    public void setDraftAccount(BetSlipDto draftAccount) {
        this.draftAccount = draftAccount;
    }

    public BetSlipDto getDraftAccount() {
        return draftAccount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public BrokerType getBrokerType() { return brokerType; }
    public void setBrokerType(BrokerType brokerType) { this.brokerType = brokerType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public List<TransactionDto> getTransactionHistory() { return transactionHistory; }
    public void setTransactionHistory(List<TransactionDto> transactionHistory) { this.transactionHistory = transactionHistory; }

    public List<BetSlipDto> getBetHistory() { return betHistory; }
    public void setBetHistory(List<BetSlipDto> betHistory) { this.betHistory = betHistory; }
}
