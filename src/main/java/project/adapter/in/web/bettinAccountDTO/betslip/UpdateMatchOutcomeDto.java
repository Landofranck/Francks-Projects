package project.adapter.in.web.bettinAccountDTO.betslip;

import jakarta.validation.constraints.NotNull;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.League;

public record UpdateMatchOutcomeDto (@NotNull String matchKey, @NotNull String outComeName,@NotNull League league,@NotNull BetStatus status){
}
