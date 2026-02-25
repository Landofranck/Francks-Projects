package project.adapter.in.web.BettingAccount.bettinAccountDTO.betslip;

import jakarta.validation.constraints.NotNull;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.League;
import project.domain.model.Enums.MatchKey;

public record UpdateMatchOutcomeDto ( MatchKey matchKey, String outComeName, League league, @NotNull BetStatus status){
}
