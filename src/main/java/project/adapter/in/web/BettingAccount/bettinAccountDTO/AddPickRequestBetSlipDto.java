package project.adapter.in.web.BettingAccount.bettinAccountDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddPickRequestBetSlipDto(
        @NotNull
        Long matchId,
        @NotEmpty
        String outComeName

) {
}