package project.adapter.in.web;

import jakarta.enterprise.context.ApplicationScoped;
import project.adapter.in.web.BettinAccountDTO.BetSlipDto;
import project.adapter.in.web.BettinAccountDTO.BettingAccountDto;
import project.adapter.in.web.BettinAccountDTO.CreateBettingAccountDto;
import project.adapter.in.web.MobileMoneyDto.CreateMobileMoneyAccountDto;
import project.adapter.in.web.MobileMoneyDto.ReadMomoAccountDto;
import project.adapter.in.web.TransactionDTO.TransactionDto;
import project.domain.model.BetSlip;
import project.domain.model.BettingAccount;
import project.domain.model.MobileMoneyAccount;
import project.domain.model.Transaction;

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
        if (acc.getTransactionHistory() != null) {
            dto.setTransactionHistory(acc.getTransactionHistory().stream().map(this::toTransactionDto).collect(Collectors.toCollection(ArrayList::new)));
        }
        if (acc.getTransactionHistory() != null) {
            dto.setBetHistory(acc.getBetHistory().stream().map(this::toBetSlipDto).collect(Collectors.toCollection(ArrayList::new)));
        }
        return dto;
    }

    public BetSlipDto toBetSlipDto(BetSlip domain) {
        var dto = new BetSlipDto();
        dto.setId(domain.getId());
        dto.setStake(domain.getStake().getValue());
        dto.setCreatedAt(domain.getCreatedAt());
        dto.setTotalOdds(domain.getTotalOdds());
        dto.setCategory(domain.getCategory());
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

    public List<BettingAccountDto> toBettingAccountDtos(List<project.domain.model.BettingAccount> accounts) {
        return accounts.stream().map(this::toBettingAccountDto).toList();
    }

    public List<ReadMomoAccountDto> toMobileMoneyDtos(List<MobileMoneyAccount> accounts) {
        return accounts.stream().map(this::toMobileMoneyDto).toList();
    }
}
