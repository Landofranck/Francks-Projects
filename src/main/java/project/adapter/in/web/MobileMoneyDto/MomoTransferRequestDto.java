package project.adapter.in.web.MobileMoneyDto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class MomoTransferRequestDto {
    @NotNull
    public Long fromAccountId;
    @NotNull
    public Long toAccountId;

    @NotNull
    @Positive
    public BigDecimal amount;
    public String description;
}