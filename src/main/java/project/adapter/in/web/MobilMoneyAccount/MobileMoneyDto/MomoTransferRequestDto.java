package project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class MomoTransferRequestDto {
    public Long fromAccountId;
    public Long toAccountId;
    @NotNull
    @Positive
    public BigDecimal amount;
    @NotEmpty
    public String description;
}