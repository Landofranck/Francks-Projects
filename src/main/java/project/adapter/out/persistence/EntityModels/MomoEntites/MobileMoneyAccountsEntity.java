package project.adapter.out.persistence.EntityModels.MomoEntites;

import jakarta.persistence.*;
import project.domain.model.Enums.MomoAccountType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "mobile_money_account",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"id","account_name", "account_type"}
        )
)
public class MobileMoneyAccountsEntity {
    @Id
    @Column(name = "id",nullable = false)
    private Long id;
    @Column(name = "account_name",nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name="account_type",nullable = false)
    public MomoAccountType accountType;
    private BigDecimal accountBalance;
    private Boolean dailyLimit;
    private Boolean weeklyLimit;
    private Boolean monthlyLimit;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MomoAccountTransactionEntity> transactionHistory=new ArrayList<>();

    public MobileMoneyAccountsEntity() {
    }


    public void addBettingaccoutTransactionEntity(MomoAccountTransactionEntity transaction) {
        this.transactionHistory.add(transaction);
        transaction.setOwner(this);
    }

    public void setTransactionEntityHistory(List<MomoAccountTransactionEntity> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public List<MomoAccountTransactionEntity> getTransactionHistory() {
        return transactionHistory;
    }

    public MomoAccountType getAccountType() {
        return accountType;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public Boolean getDailyLimit() {
        return dailyLimit;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Boolean getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setDailyLimit(Boolean dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public Boolean getWeeklyLimit() {
        return weeklyLimit;
    }

    public void setWeeklyLimit(Boolean weeklyLimit) {
        this.weeklyLimit = weeklyLimit;
    }

    public void setMonthlyLimit(Boolean monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public Long getId() {
        return id;
    }

    public void setAccountType(MomoAccountType accountType) {
        this.accountType = accountType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
