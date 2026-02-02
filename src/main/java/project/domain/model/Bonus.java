package project.domain.model;

import project.domain.model.Enums.BonusStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class Bonus {
    private Money amount;
    private Instant expiryDate;
    private BonusStatus status;
    public Bonus(BigDecimal amount,Instant expiry,BonusStatus status) {
        this.amount = new Money(amount);
        this.expiryDate = expiry;
        this.status = status;
    }

    public void updateStatus(){
        if(Instant.now().isAfter(expiryDate)&&status!=BonusStatus.EXPIRED){
            status=BonusStatus.EXPIRED;
        }
    }
    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public BonusStatus getStatus() {
        return status;
    }

    public void setStatus(BonusStatus status) {
        this.status = status;
    }
}
