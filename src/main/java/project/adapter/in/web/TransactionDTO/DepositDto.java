package project.adapter.in.web.TransactionDTO;


import java.math.BigDecimal;

public class DepositDto {
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

