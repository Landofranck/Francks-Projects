package project.adapter.in.web.bettinAccountDTO;

import jakarta.validation.constraints.NotNull;
import project.domain.model.Enums.BonusStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record BonusDto (@NotNull BigDecimal amount, @NotNull Instant expiryDate, @NotNull BonusStatus status){
}
