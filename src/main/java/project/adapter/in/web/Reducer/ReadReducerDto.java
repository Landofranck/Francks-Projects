package project.adapter.in.web.Reducer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import project.adapter.in.web.Utils.Link;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.ReadMatchDto;
import project.domain.model.Enums.BetStrategy;
import project.domain.model.Enums.BrokerType;

import java.math.BigDecimal;
import java.util.List;

public record ReadReducerDto(
        @NotNull
        Long id,
        BigDecimal totalStake,
        @PositiveOrZero
        int numberOfSlips,
        String specifications,
        @NotNull
        BetStrategy strategy,
        @NotNull
        BrokerType broker,
        List<ReadMatchDto> betMatchDtos,
        List<ReducerSlipDto> slips,
        BigDecimal bonusAmount,
        List<Link> links
) {

}
