package project.adapter.in.web.TransactionDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import project.adapter.in.web.MatchDto;
import project.adapter.in.web.bettinAccountDTO.betslip.BetSlipDto;
import project.adapter.out.persistence.EntityModels.BetSlipEntity;
import project.adapter.out.persistence.EntityModels.MatchEntity;

import java.math.BigDecimal;
import java.util.List;

public record ReadReducerDto(

        Long id,
        BigDecimal totalStake,
        List<MatchDto> betMatchDtos,
        List<BetSlipDto> slips,
        BigDecimal bonusAmount) {


}
