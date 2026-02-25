package project.adapter.in.web.Utils.Mapper;

import jakarta.enterprise.context.ApplicationScoped;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.*;
import project.adapter.in.web.Reducer.*;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.betslip.BetHistoryDto;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.betslip.DraftDto;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.betslip.ReadBetSlipDto;
import project.adapter.in.web.TransactionDTO.TransactionDto;
import project.domain.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BettingDTOMapper {


    public GetBettingAccountDto toGetBettingAccountDto(BettingAccount acc) {
        var dto = new GetBettingAccountDto();
        dto.setId(acc.getAccountId());
        dto.setAccountName(acc.getAccountName());
        dto.setBrokerType(acc.getAccountType());
        dto.setBalance(acc.getBalance().getValue());
        if (acc.getDraftBetSlip() != null) {
            dto.setDraftAccount(toDraftSlipDto(acc.getDraftBetSlip()));
        }
        if (acc.getBonuses() != null) {
            dto.setBonuses(acc.getBonuses().stream().map(this::toBonusDto).collect(Collectors.toCollection(ArrayList::new)));
        }
        return dto;
    }

    public BonusDto toBonusDto(Bonus b) {
        return new BonusDto(b.getAmount().getValue(), b.getExpiryDate(), b.getStatus(),b.getType());
    }

    public DraftDto toDraftSlipDto(DraftBetSlip domain) {
        var dto = new DraftDto();
        dto.setParentBettingAccountId(domain.getDraftSlipOwner() != null ? domain.getDraftSlipOwner().getAccountId() : null);
        dto.setId(domain.getId());
        dto.setPotentialWinning(domain.getPotentialWinning().getValue());
        dto.setStake(domain.getStake().getValue());
        dto.setCreatedAt(domain.getCreatedAt());
        dto.setTotalOdds(domain.getTotalOdds());
        dto.setCategory(domain.getCategory());
        dto.setStatus(domain.getStatus());
        dto.setBonusSlip(domain.getBonusSlip());
        dto.setStrategy(domain.getStrategy());
        dto.setPicks(domain.getPicks().stream().map(this::toMatchEventPickDto).collect(Collectors.toCollection(ArrayList::new)));
        return dto;
    }


    public ReadBetSlipDto toBetSlipDto(BetSlip domain) {
        var dto = new ReadBetSlipDto();
        dto.setParentBettingAccountId(domain.getParentAccountId() != null ? domain.getParentAccountId() : null);
        dto.setId(domain.getId());
        dto.setStatus(domain.getStatus());
        dto.setPotentialWinning(domain.getPotentialWinning().getValue());
        dto.setStake(domain.getStake().getValue());
        dto.setCreatedAt(domain.getCreatedAt());
        dto.setTotalOdds(domain.getTotalOdds());
        dto.setCategory(domain.getCategory());
        dto.setBonusSlip(domain.getBonusSlip());
        dto.setBonusOdds(domain.getBonusOdds());
        dto.setStrategy(domain.getStrategy());
        dto.setPicks(domain.getPicks().stream().map(this::toMatchEventPickDto).collect(Collectors.toCollection(ArrayList::new)));
        return dto;
    }





    public TransactionDto toTransactionDto(Transaction domainModel) {
        var dtoModel = new TransactionDto();
        dtoModel.setId(domainModel.getId());
        dtoModel.setTransactionAmount(domainModel.getTransactionAmmount().getValue());
        dtoModel.setBalanceAfter(domainModel.getAccountBalanceAfterTransaction().getValue());
        dtoModel.setType(domainModel.getType());
        dtoModel.setDescription(domainModel.getDescription());
        dtoModel.setBetSlipId(domainModel.getBetslipId());
        dtoModel.setCreatedAt(domainModel.getCreatedAt());
        dtoModel.setOwnerId(domainModel.getOwnerId());
        return dtoModel;
    }


    public List<GetBettingAccountDto> toBettingAccountDtos(List<BettingAccount> accounts) {
        return accounts.stream().map(this::toGetBettingAccountDto).toList();
    }


    public Match toMatchDomain(CreateMatchDto dto) {
        var domain = new Match(dto.getHome(), dto.getAway(), dto.getBroker(),dto.getBegins(),dto.getEnds());
        domain.setBonusMatch(dto.isBonusMatch());
        domain.setMatchLeague(dto.getMatchLeague());
        for (CreateMatchEventPickDto o : dto.getMatchOutComes()) {
            domain.addPick(toMatchEventpick(o));
        }
        return domain;
    }

    public List<MatchOutComePick> matchOutComePickList(UpdateMatchDto dto) {

        List<MatchOutComePick> out = new ArrayList<>();
        for (CreateMatchEventPickDto o : dto.matchOutComes()) {
            out.add(toMatchEventpick(o));
        }
        return out;
    }

    private MatchOutComePick toMatchEventpick(CreateMatchEventPickDto eventDto) {
        var pick = new MatchOutComePick(eventDto.getMatchId(), eventDto.getMatchKey(), eventDto.getOutcomeName(), eventDto.getOdd(), eventDto.getLeague());
        pick.setOutcomePickStatus(eventDto.getStatus());
        return pick;
    }

    public List<ReadMatchDto> toMatchDtos(List<Match> matches) {
        return matches.stream().map(this::toMatchDto).collect(Collectors.toCollection(ArrayList::new));
    }

    public ReadMatchDto toMatchDto(Match match) {
        var dto = new ReadMatchDto();
        dto.setId(match.getMatchId());
        dto.setAway(match.getAway());
        dto.setHome(match.getHome());
        dto.setBonusMatch(match.isBonusMatch());
        dto.setBegins(match.getBegins());
        dto.setEnds(match.getEnds());
        dto.setMatchLeague(match.getMatchLeague());
        dto.setBroker(match.getBroker());
        dto.setMatchOutComes(match.getMatchOutComes().stream().map(this::toMatchEventPickDto).collect(Collectors.toCollection(ArrayList::new)));

        return dto;
    }

    public ReadMatchEventPickDto toMatchEventPickDto(MatchOutComePick mP) {
        var dto = new ReadMatchEventPickDto();
        dto.setMatchKey(mP.getMatchKey());
        dto.setOdd(mP.getOdd());
        dto.setOwnerMatchName(mP.getOwnerMatchName());
        dto.setMatchId(mP.getIdentity());
        dto.setOutcomeName(mP.getOutcomeName());
        dto.setStatus(mP.getOutcomePickStatus());
        dto.setLeague(mP.getLeague());
        return dto;
    }

    public Bonus toBonusDomain(BonusDto bonus) {
        return new Bonus(bonus.amount(), bonus.expiryDate(), bonus.status(), bonus.type());
    }

    public BetHistoryDto toBetHistoryDto(List<BetSlip> history) {
        return new BetHistoryDto(history.stream().map(this::toBetSlipDto).collect(Collectors.toCollection(ArrayList::new))) ;
    }

    public TransactionHistoryDto toTransactionHistoryDto(List<Transaction> history) {
        return new TransactionHistoryDto(history.stream().map(this::toTransactionDto).collect(Collectors.toCollection(ArrayList::new)));
    }



    public BettingAccount toBettingAccountDomain(CreateBettingAccountDto dto) {
        return new BettingAccount(dto.getAccountName(),dto.getBrokerType());
    }

}
