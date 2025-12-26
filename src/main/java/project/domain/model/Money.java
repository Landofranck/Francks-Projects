package project.domain.model;


import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
@Embeddable
public final class Money {

    private static final int SCALE = 2; // cents
    private static final RoundingMode ROUNDING = RoundingMode.HALF_EVEN;

    private final BigDecimal value;

    public Money(BigDecimal value) {
        Objects.requireNonNull(value, "money value must not be null");

        BigDecimal normalized = value.setScale(SCALE, ROUNDING);
        if (normalized.signum() < 0) {
            throw new IllegalArgumentException("money cannot be negative");
        }
        this.value = normalized;
    }

    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public BigDecimal getValue() {
        return value;
    }

    public Money add(Money other) {
        Objects.requireNonNull(other);
        return new Money(this.value.add(other.value));
    }

    public Money subtract(Money other) {
        Objects.requireNonNull(other);
        BigDecimal result = this.value.subtract(other.value);
        if (result.signum() < 0) {
            throw new IllegalArgumentException("insufficient funds");
        }
        return new Money(result);
    }

    public boolean isGreaterThan(Money other) {
        Objects.requireNonNull(other);
        return this.value.compareTo(other.value) > 0;
    }

    public boolean isGreaterOrEqual(Money other) {
        Objects.requireNonNull(other);
        return this.value.compareTo(other.value) >= 0;
    }

    public boolean isZero() {
        return value.signum() == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return value.compareTo(money.value) == 0;
    }

    @Override
    public int hashCode() {
        return value.stripTrailingZeros().hashCode();
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
}
