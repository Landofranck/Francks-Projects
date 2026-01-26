package project.adapter.in.web;

import jakarta.validation.constraints.NotNull;
import project.domain.model.Money;

import java.math.BigDecimal;

public class CreateReducerDto {

    @NotNull(message = "TotalStake must have a value")
    private BigDecimal totalStake;

    @NotNull(message = "bonus amount must not be null")
    private BigDecimal bonusAmount;


    public BigDecimal getBonusAmount() {
        return bonusAmount;
    }

    public BigDecimal getTotalStake() {
        return totalStake;
    }

}
