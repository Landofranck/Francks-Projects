package project.adapter.in.web;

import jakarta.enterprise.context.ApplicationScoped;
import project.adapter.in.web.TransactionDTO.ReadReducerDto;
import project.adapter.in.web.bettinAccountDTO.betslip.BetSlipDto;
import project.adapter.in.web.bettinAccountDTO.BettingAccountDto;
import project.adapter.in.web.bettinAccountDTO.CreateBettingAccountDto;
import project.adapter.in.web.MobileMoneyDto.CreateMobileMoneyAccountDto;
import project.adapter.in.web.MobileMoneyDto.ReadMomoAccountDto;
import project.adapter.in.web.TransactionDTO.TransactionDto;
import project.domain.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class DTOMapper {
    public BettingAccount toBettingAccountDomain(CreateBettingAccountDto dto) {
        Objects.requireNonNull(dto, "dto");
        Objects.requireNonNull(dto.getAccountName(), "accountName");
        Objects.requireNonNull(dto.getBrokerType(), "brokerType");

        return new BettingAccount(dto.getAccountName(), dto.getBrokerType());
    }

    public BettingAccountDto toBettingAccountDto(BettingAccount acc) {
        var dto = new BettingAccountDto();
        dto.setId(acc.getAccountId());
        dto.setAccountName(acc.getAccountName());
        dto.setBrokerType(acc.getAccountType());
        dto.setBalance(acc.getBalance().getValue());
        if (acc.getDraftBetSlip() != null) {
            dto.setDraftAccount(toDraftSlipDto(acc.getDraftBetSlip()));
        }
        if (acc.getTransactionHistory() != null) {
            dto.setTransactionHistory(acc.getTransactionHistory().stream().map(this::toTransactionDto).collect(Collectors.toCollection(ArrayList::new)));
        }
        if (acc.getBetHistory() != null) {
            dto.setBetHistory(acc.getBetHistory().stream().map(this::toBetSlipDto).collect(Collectors.toCollection(ArrayList::new)));
        }
        return dto;
    }

    public BetSlipDto toDraftSlipDto(DraftBetSlip domain) {
        var dto = new BetSlipDto();
        dto.setParentBettingAccountId(domain.getDraftSlipOwner() != null ? domain.getDraftSlipOwner().getAccountId() : null);
        dto.setId(domain.getId());
        dto.setPotentialWinning(domain.getPotentialWinning().getValue());
        dto.setStake(domain.getStake().getValue());
        dto.setCreatedAt(domain.getCreatedAt());
        dto.setTotalOdds(domain.getTotalOdds());
        dto.setCategory(domain.getCategory());
        dto.setStatus(domain.getStatus());
        dto.setPicks(domain.getPicks().stream().map(this::toMatchEventPickDto).collect(Collectors.toCollection(ArrayList::new)));
        return dto;
    }


    public BetSlipDto toBetSlipDto(BetSlip domain) {
        var dto = new BetSlipDto();
        dto.setParentBettingAccountId(domain.getParentAccount() != null ? domain.getParentAccount().getAccountId() : null);
        dto.setId(domain.getId());
        dto.setStatus(domain.getStatus());
        dto.setPotentialWinning(domain.getPotentialWinning().getValue());
        dto.setStake(domain.getStake().getValue());
        dto.setCreatedAt(domain.getCreatedAt());
        dto.setTotalOdds(domain.getTotalOdds());
        dto.setCategory(domain.getCategory());
        dto.setPicks(domain.getPicks().stream().map(this::toMatchEventPickDto).collect(Collectors.toCollection(ArrayList::new)));
        return dto;
    }

    public MobileMoneyAccount toMobileMoneyDomain(Long id, CreateMobileMoneyAccountDto dto) {
        Objects.requireNonNull(dto, "dto");
        Objects.requireNonNull(dto.accountType, "accountType");
        return new MobileMoneyAccount(id, dto.accountType);
    }

    public TransactionDto toTransactionDto(Transaction domainModel) {
        var dtoModel = new TransactionDto();
        dtoModel.setId(domainModel.getId());
        dtoModel.setTransactionAmount(domainModel.getTransactionAmmount().getValue());
        dtoModel.setBalanceAfter(domainModel.getAccountBalanceAfterTransaction().getValue());
        dtoModel.setType(domainModel.getType());
        dtoModel.setDescription(domainModel.getDescription());
        dtoModel.setCreatedAt(domainModel.getCreatedAt());
        return dtoModel;
    }

    public ReadMomoAccountDto toMobileMoneyDto(MobileMoneyAccount acc) {
        var dto = new ReadMomoAccountDto();
        dto.setId(acc.getAccountId());
        dto.setAccountBalance(acc.getAccountBalance().getValue());
        dto.setAccountType(acc.accountType);
        dto.setDailyLimit(acc.getDailyLimit());
        dto.setWeeklyLimit(acc.getWeeklyLimit());
        dto.setMonthlyLimit(acc.getMonthlyLimit());
        if (acc.getTransactionHistory() != null) {
            dto.setTransactionHistory(acc.getTransactionHistory().stream().map(this::toTransactionDto).collect(Collectors.toCollection(ArrayList::new)));
        }
        return dto;
    }

    public List<BettingAccountDto> toBettingAccountDtos(List<BettingAccount> accounts) {
        return accounts.stream().map(this::toBettingAccountDto).toList();
    }

    public List<ReadMomoAccountDto> toMobileMoneyDtos(List<MobileMoneyAccount> accounts) {
        return accounts.stream().map(this::toMobileMoneyDto).toList();
    }

    public Match toMatchDomain(MatchDto dto) {
        if (dto.getMatchOutComes() == null || dto.getMatchOutComes().isEmpty())
            throw new IllegalArgumentException("match must have outcomes :Dto mapper line 94");

        var domain = new Match(dto.getHome(), dto.getAway());
        for (MatchEventPickDto o : dto.getMatchOutComes()) {
            domain.addPick(toMatchEventpick(o));
        }

        domain.setMatchId(dto.getId());
        return domain;
    }

    private MatchEventPick toMatchEventpick(MatchEventPickDto eventDto) {
        var pick = new MatchEventPick(eventDto.getMatchKey(), eventDto.getOutcomeName(), eventDto.getOdd());
        return pick;
    }

    public List<MatchDto> toMatchDtos(List<Match> matches) {
        return matches.stream().map(this::toMatchDto).collect(Collectors.toCollection(ArrayList::new));
    }

    private MatchDto toMatchDto(Match match) {
        var dto = new MatchDto();
        dto.setId(match.getMatchId());
        dto.setAway(match.getAway());
        dto.setHome(match.getHome());
        dto.setMatchOutComes(match.getMatchOutComes().stream().map(this::toMatchEventPickDto).collect(Collectors.toCollection(ArrayList::new)));

        return dto;
    }

    private MatchEventPickDto toMatchEventPickDto(MatchEventPick mP) {
        var dto = new MatchEventPickDto();
        dto.setMatchKey(mP.getMatchKey());
        dto.setOdd(mP.getOdd());
        dto.setOutcomeName(mP.getOutcomeName());
        return dto;
    }

    public ReadReducerDto toReducerDto(Reducer domain) {
        List<BetSlipDto> betslips = new ArrayList<>();
        List<MatchDto> matches = new ArrayList<>();
        if (domain.getBetMatches() != null)
            matches = domain.getBetMatches().stream().map(this::toMatchDto).collect(Collectors.toCollection(ArrayList::new));
        if (domain.getSlips() != null)
            betslips = domain.getSlips().stream().map(this::toBetSlipDto).collect(Collectors.toCollection(ArrayList::new));

        var out = new ReadReducerDto(domain.getAccountId(), domain.getTotalStake().getValue(), matches, betslips, domain.getBonusAmount().getValue());
        return out;
    }

    public Reducer toReducerDomain(CreateReducerDto dto) {
        var out = new Reducer(new Money(dto.getTotalStake()), new Money(dto.getBonusAmount()));
        return out;
    }


    public BetSlip toBetSlipDomain(BetSlipDto slip) {
        var domain = new BetSlip(slip.getCategory());
        domain.setStake(new Money(slip.getStake()));
        domain.setTotalOdds(slip.getTotalOdds());
        domain.setStatus(slip.getStatus());
        return domain;
    }
}
