package project.adapter.in.web;


import java.math.BigDecimal;

public class DepositDto {
    private BigDecimal amount;

    public DepositDto() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

