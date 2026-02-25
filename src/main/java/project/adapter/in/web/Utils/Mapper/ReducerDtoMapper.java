package project.adapter.in.web.Utils.Mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.ReadMatchDto;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.betslip.ReadBetSlipDto;
import project.adapter.in.web.Reducer.*;
import project.domain.model.BetSlip;
import project.domain.model.Money;
import project.domain.model.Reducer.Block;
import project.domain.model.Reducer.Reducer;
import project.domain.model.Reducer.ReducerBetSlip;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReducerDtoMapper {
    @Inject
    BettingDTOMapper betMapper;

    public Reducer toReducerDomain(CreateReducerDto dto) {
        return new Reducer(new Money(dto.getTotalStake()), new Money(dto.getBonusAmount()), dto.getBetStrategy(), dto.getBroker());
    }

    public BetSlip toBetSlipDomain(ReadBetSlipDto slip) {
        var domain = new BetSlip(slip.getBonusSlip(), slip.getStrategy());
        domain.setStake(new Money(slip.getStake()));
        domain.setTotalOdds(slip.getTotalOdds());
        domain.setStatus(slip.getStatus());
        domain.setPotentialWinning(new Money(slip.getPotentialWinning()));
        domain.setBonusOdds(slip.getBonusOdds());

        return domain;
    }

    public ReadReducerDto toReducerDto(Reducer domain) {

        // blocks -> lines -> specifics
        String specifics = Optional.ofNullable(domain.getBlocks())
                .orElseGet(List::of)
                .stream()
                .map(b -> b.getType() + ": " + (b.getEndMatchIdx() - b.getStartMatchIdx() + 1))
                .collect(Collectors.joining(System.lineSeparator()));

        // matches (null-safe)
        List<ReadMatchDto> matches = Optional.ofNullable(domain.getBetMatches())
                .orElseGet(List::of)
                .stream()
                .map(betMapper::toMatchDto)
                .toList();

        // slips (null-safe)
        List<ReducerSlipDto> betSlips = Optional.ofNullable(domain.getSlips())
                .orElseGet(List::of)
                .stream()
                .map(this::toReducerSlipDto)
                .toList();

        int slipCount = betSlips.size();

        BigDecimal totalStake = domain.getTotalStake().getValue();
        BigDecimal bonusAmount = domain.getBonusAmount().getValue();

        return new ReadReducerDto(
                domain.getAccountId(),
                totalStake,
                slipCount,
                specifics,
                domain.getStrategy(),
                domain.getBroker(),
                matches,
                betSlips,
                bonusAmount,
                new ArrayList<>()
        );
    }

    public ReducerSlipDto toReducerSlipDto(ReducerBetSlip domain) {
        var picks = domain.getPicks().stream().map(betMapper::toMatchEventPickDto).collect(Collectors.toCollection(ArrayList::new));
        return new ReducerSlipDto(domain.getCategory(), domain.getBrokerType(), domain.getPlanedStake().getValue(), domain.getRemainingStake().getValue(), domain.getTotalOdds(), domain.getBetStrategy(), domain.getNumberOfEvents(), domain.getPotentialWinning().getValue(), domain.getBonusOdds(),picks);
    }

    public Block toBlockDomain(BlockDto dto) {
        return new Block(dto.type(), dto.start(), dto.end());
    }

    public BlockDto toBlockDto(Block dom) {
        return new BlockDto(dom.getType(), dom.getStartMatchIdx(), dom.getEndMatchIdx());
    }

    public List<Block> toListOfBlocks(ComputeDto specifications) {
        return specifications.specifications().stream().map(this::toBlockDomain).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<ReadReducerDto> toReducerDomains(List<Reducer> reducers) {
        return reducers.stream().map(this::toReducerDto).toList();
    }
}
