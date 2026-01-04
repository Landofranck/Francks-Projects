package project.adapter.in.web;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import project.domain.model.Money;

import java.math.BigDecimal;
import java.util.List;

public class ReducerDto {
    private Long id;
    @NotNull(message = "TotalStake must have a value")
    private BigDecimal totalStake;

    @NotNull(message = "bonus amount must not be null")
    private Money bonusAmount;







    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setBonusAmount(Money bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public void setTotalStake(BigDecimal totalStake) {
        this.totalStake = totalStake;
    }

    public Money getBonusAmount() {
        return bonusAmount;
    }

    public BigDecimal getTotalStake() {
        return totalStake;
    }

}
