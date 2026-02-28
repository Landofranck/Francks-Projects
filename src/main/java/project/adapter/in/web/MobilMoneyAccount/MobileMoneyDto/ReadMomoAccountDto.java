package project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto;

import project.adapter.in.web.Utils.Link;
import project.domain.model.Enums.MomoAccountType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReadMomoAccountDto {
    private Long id;
    private MomoAccountType accountType;
    private BigDecimal accountBalance;
    private BigDecimal dailyLimitAmount;
    private BigDecimal weeklyLimitAmount;
    private BigDecimal monthlyLimitAmount;
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

    public MomoAccountType getAccountType() {
        return accountType;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }


    public void setLinks(List<Link> selfLinks) {
        this.links = selfLinks;
    }

    public List<Link> getLinks() {
        return links;
    }

    public BigDecimal getDailyLimitAmount() {
        return dailyLimitAmount;
    }

    public void setDailyLimitAmount(BigDecimal dailyLimitAmount) {
        this.dailyLimitAmount = dailyLimitAmount;
    }

    public BigDecimal getWeeklyLimitAmount() {
        return weeklyLimitAmount;
    }

    public void setWeeklyLimitAmount(BigDecimal weeklyLimitAmount) {
        this.weeklyLimitAmount = weeklyLimitAmount;
    }

    public BigDecimal getMonthlyLimitAmount() {
        return monthlyLimitAmount;
    }

    public void setMonthlyLimitAmount(BigDecimal monthlyLimitAmount) {
        this.monthlyLimitAmount = monthlyLimitAmount;
    }
}
