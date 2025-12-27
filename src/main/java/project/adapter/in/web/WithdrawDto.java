package project.adapter.in.web;

import java.math.BigDecimal;

public class WithdrawDto {
    private BigDecimal amount;

    public WithdrawDto() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
