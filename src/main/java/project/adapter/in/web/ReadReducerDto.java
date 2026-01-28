package project.adapter.in.web;

import project.adapter.in.web.bettinAccountDTO.betslip.BetSlipDto;

import java.math.BigDecimal;
import java.util.List;

public record ReadReducerDto(

        Long id,
        BigDecimal totalStake,
        int numberOfSlips,
        List<MatchDto> betMatchDtos,
        List<BetSlipDto> slips,
        BigDecimal bonusAmount
        ) {


}
