package project.adapter.in.web.TransactionDTO;


import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class DepositDto {
    @NotNull
    private Long bettingAccountId;
    @NotNull
    private BigDecimal amount;

    private String withdrawalDescription;
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public DepositDto() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setWithdrawalDescription(String withdrawalDescription) {
        this.withdrawalDescription = withdrawalDescription;
    }

    public String getWithdrawalDescription() {
        return withdrawalDescription;
    }

    public void setBettingAccountId(Long bettingAccountId) {
        this.bettingAccountId = bettingAccountId;
    }

    public Long getBettingAccountId() {
        return bettingAccountId;
    }
}

