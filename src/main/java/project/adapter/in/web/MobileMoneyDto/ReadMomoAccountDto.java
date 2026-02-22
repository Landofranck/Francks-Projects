package project.adapter.in.web.MobileMoneyDto;

import project.adapter.in.web.Utils.Link;
import project.domain.model.Enums.MomoAccountType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReadMomoAccountDto {
    private Long id;
    private MomoAccountType accountType;
    private BigDecimal accountBalance;
    private Boolean dailyLimit;
    private Boolean weeklyLimit;
    private Boolean monthlyLimit;
    private List<Link> links=new ArrayList<>();

    public void addLink(Link link) {
        this.links.add(link);
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setAccountType(MomoAccountType accountType) {
        this.accountType = accountType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMonthlyLimit(Boolean monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public void setDailyLimit(Boolean dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public void setWeeklyLimit(Boolean weeklyLimit) {
        this.weeklyLimit = weeklyLimit;
    }

    public MomoAccountType getAccountType() {
        return accountType;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public Boolean getDailyLimit() {
        return dailyLimit;
    }

    public Boolean getWeeklyLimit() {
        return weeklyLimit;
    }

    public Boolean getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setLinks(List<Link> selfLinks) {
        this.links = selfLinks;
    }

    public List<Link> getLinks() {
        return links;
    }
}
