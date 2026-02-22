package project.adapter.out.persistence.EntityModels.BettingAccount;

import jakarta.persistence.Embeddable;
import project.domain.model.Enums.BonusStatus;
import project.domain.model.Enums.BonusType;
import project.domain.model.Money;

import java.math.BigDecimal;
import java.time.Instant;

@Embeddable
public class BonusEmb {
    private BigDecimal amount;
    private Instant expiryDate;
    private BonusStatus status;
    private BonusType type;

    public BonusEmb() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BonusStatus getStatus() {
        return status;
    }

    public void setStatus(BonusStatus status) {
        this.status = status;
    }

    public BonusType getType() {
        return this.type;
    }

    public void setType(BonusType type) {
        this.type = type;
    }
}
