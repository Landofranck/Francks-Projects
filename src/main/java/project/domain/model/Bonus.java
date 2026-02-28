package project.domain.model;

import project.domain.model.Enums.BonusStatus;
import project.domain.model.Enums.BonusType;

import java.math.BigDecimal;
import java.time.Instant;

public class Bonus {
    private Money amount;
    private Instant expiryDate;
    private BonusStatus status;
    private BonusType type;

    public Bonus(BigDecimal amount, Instant expiry, BonusType type) {
        this.amount = new Money(amount);
        this.expiryDate = expiry;
        if(expiry.isAfter(Instant.now())){
            this.status = BonusStatus.ACTIVE;
        }else {
            this.status=BonusStatus.EXPIRED;
        }
        this.type = type;
    }

    public void updateStatus() {
        if (Instant.now().isAfter(expiryDate) && status != BonusStatus.EXPIRED) {
            status = BonusStatus.EXPIRED;
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

    public void setType(BonusType type) {
        this.type = type;
    }

    public BonusType getType() {
        return type;
    }
}
