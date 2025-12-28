package project.adapter.in.web;

import jakarta.enterprise.context.ApplicationScoped;
import project.adapter.in.web.BettinAccountDTO.BettingAccountDto;
import project.adapter.in.web.BettinAccountDTO.CreateBettingAccountDto;
import project.adapter.in.web.MobileMoneyDto.MobileMoneyAccountDto;
import project.domain.model.BettingAccount;
import project.domain.model.MobileMoneyAccount;

import java.util.List;
import java.util.Objects;

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
        dto.setTransactionHistory(null);
        dto.setBetHistory(null);
        return dto;
    }

    public MobileMoneyAccount toMobileMoneyDomain(Long id, MobileMoneyAccountDto dto) {
        Objects.requireNonNull(dto, "dto");
        Objects.requireNonNull(dto.accountType, "accountType");
        return new MobileMoneyAccount(id, dto.accountType);
    }

    public MobileMoneyAccountDto toMobileMoneyDto(project.domain.model.MobileMoneyAccount acc) {
        var dto = new MobileMoneyAccountDto();
        dto.setId(acc.getAccountId());
        dto.setAccountType(acc.getAccountType());
        return dto;
    }
    public List<BettingAccountDto> toBettingAccountDtos(List<project.domain.model.BettingAccount> accounts) {
        return accounts.stream().map(this::toBettingAccountDto).toList();
    }

    public List<MobileMoneyAccountDto> toMobileMoneyDtos(List<project.domain.model.MobileMoneyAccount> accounts) {
        return accounts.stream().map(this::toMobileMoneyDto).toList();
    }
}
