package project.adapter.in.web.bettinAccountDTO;

import project.adapter.in.web.Utils.Link;
import project.adapter.in.web.bettinAccountDTO.betslip.DraftDto;
import project.domain.model.Enums.BrokerType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GetBettingAccountDto {
    private Long id;
    private String accountName;
    private BrokerType brokerType;
    private BigDecimal balance;
    private Link transactionHistory;
    private Link betHistory;
    private DraftDto draftAccount;


    private List<Link> links=new ArrayList<>();
    public List<BonusDto> getBonuses() {
        return bonuses;
    }

    public void setBonuses(List<BonusDto> bonuses) {
        this.bonuses = bonuses;
    }

    private List<BonusDto> bonuses;

    public GetBettingAccountDto() {
    }


    public void setDraftAccount(DraftDto draftAccount) {
        this.draftAccount = draftAccount;
    }

    public DraftDto getDraftAccount() {
        return draftAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BrokerType getBrokerType() {
        return brokerType;
    }

    public void setBrokerType(BrokerType brokerType) {
        this.brokerType = brokerType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Link> getSelfLinks() {
        return links;
    }

    public void setSelfLinks(List<Link> selfLinks) {
        this.links = selfLinks;
    }

    public Link getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(Link transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public Link getBetHistory() {
        return betHistory;
    }

    public void setBetHistory(Link betHistory) {
        this.betHistory = betHistory;
    }
}
