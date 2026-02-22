package project.adapter.in.web.TransactionDTO;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class DepositInBettingAccountDto {
    @NotNull
    private Long momoId;
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

    public DepositInBettingAccountDto() {
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

    public void setMomoId(Long momoId) {
        this.momoId = momoId;
    }

    public Long getMomoId() {
        return momoId;
    }

}
