package project.adapter.in.web.TransactionDTO;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class WithdrawDto {
    @NotNull
    private BigDecimal amount;
    private String description;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
