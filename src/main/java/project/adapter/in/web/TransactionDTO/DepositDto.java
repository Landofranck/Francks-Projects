package project.adapter.in.web.TransactionDTO;


import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class DepositDto {
    @NotNull
    private BigDecimal amount;
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
}

