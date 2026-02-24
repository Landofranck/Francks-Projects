package project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class MomoTopUpRequestDto {
    @NotNull
    @Positive
    public BigDecimal amount;
    public String description;
}

