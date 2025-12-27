package project.adapter.in.web.BettinAccountDTO;

import jakarta.validation.constraints.NotNull;
import project.domain.model.Enums.AccountType;

public class CreateBettingAccountDto {
    @NotNull(message = "accountname")
    private String accountName;
    @NotNull(message = "broker type")
    private AccountType brokerType;

    public CreateBettingAccountDto() {}

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public AccountType getBrokerType() { return brokerType; }
    public void setBrokerType(AccountType brokerType) { this.brokerType = brokerType; }
}
