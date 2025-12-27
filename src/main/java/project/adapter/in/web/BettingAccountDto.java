package project.adapter.in.web;

import jakarta.xml.bind.annotation.XmlRootElement;
import project.domain.model.Enums.AccountType;

import java.math.BigDecimal;
import java.util.List;
@XmlRootElement
public class BettingAccountDto implements AccountDto{
    private Long id; // generated
    private String accountName;
    private AccountType brokerType;
    private BigDecimal balance;
    private List<TransactionDto> transactionHistory;
    private List<BetSlipDto> betHistory;

    @Override
    public Long getId() {
        return this.id;
    }
}
