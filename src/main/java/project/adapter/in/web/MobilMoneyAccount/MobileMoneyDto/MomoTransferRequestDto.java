package project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;

public class MomoTransferRequestDto {
    public Long fromAccountId;
    public Long toAccountId;
    @NotNull
    @Positive
    public BigDecimal amount;
    @NotEmpty
    public String description;
    private Instant transactionTime;

    public void setTransactionTime(Instant transactionTime) {
        this.transactionTime = transactionTime;
    }
    public Instant getTransactionTime() {
        return transactionTime;
    }
}