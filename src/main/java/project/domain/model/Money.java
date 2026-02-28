package project.domain.model;


import jakarta.persistence.Embeddable;
import jakarta.persistence.criteria.CriteriaBuilder;
import project.adapter.in.web.Utils.Code;
import project.application.error.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;

public final class Money {

    private static final int SCALE = 2; // cents
    private static final RoundingMode ROUNDING = RoundingMode.HALF_EVEN;

    private BigDecimal value;

    public Money(BigDecimal value) {
        Objects.requireNonNull(value, "money cannot be negative money 20");

        BigDecimal normalized = value.setScale(SCALE, ROUNDING);
        if (normalized.signum() < 0) {
            throw new ValidationException(Code.INVALID_AMOUNT, "money cannot be negative money 24", Map.of());
        }
        this.value = normalized;
    }

    public Money(int val) {
        var value = BigDecimal.valueOf(val);
        Objects.requireNonNull(val, "money cannot be negative money 30");
        BigDecimal normalized = value.setScale(SCALE, ROUNDING);
        if (normalized.signum() < 0) {
            throw new ValidationException(Code.INVALID_AMOUNT, "money cannot be negative 33", Map.of());
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
    public boolean isGreaterThan(Integer other) {
        Objects.requireNonNull(other);
        var out=BigDecimal.valueOf(other);
        return this.value.compareTo(out) > 0;
    }
    public boolean isGreaterThan(Long other) {
        Objects.requireNonNull(other);
        var out=BigDecimal.valueOf(other);
        return this.value.compareTo(out) > 0;
    }

    public boolean isGreaterOrEqual(Money other) {
        Objects.requireNonNull(other);
        return this.value.compareTo(other.value) >= 0;
    }
    public boolean isGreaterThanOrEqual(Long other) {
        Objects.requireNonNull(other);
        var out=BigDecimal.valueOf(other);
        return this.value.compareTo(out) >= 0;
    }

    public Money divide(BigDecimal divisor) {
        Objects.requireNonNull(divisor, "divisor");
        if (divisor.signum() <= 0) throw new IllegalArgumentException("divisor must be > 0");

        return new Money(this.value.divide(divisor, SCALE, ROUNDING));
    }

    public Money divide(double divisor) {
        if (divisor <= 0) throw new IllegalArgumentException("divisor must be > 0");
        return divide(BigDecimal.valueOf(divisor));
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
