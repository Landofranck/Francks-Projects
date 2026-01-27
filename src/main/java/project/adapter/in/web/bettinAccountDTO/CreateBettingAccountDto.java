package project.adapter.in.web.bettinAccountDTO;

import jakarta.validation.constraints.NotNull;
import project.domain.model.Enums.BrokerType;

public class CreateBettingAccountDto {
    @NotNull(message = "accountname")
    private String accountName;
    @NotNull(message = "broker type")
    private BrokerType brokerType;

    public CreateBettingAccountDto() {}

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public BrokerType getBrokerType() { return brokerType; }
    public void setBrokerType(BrokerType brokerType) { this.brokerType = brokerType; }
}
