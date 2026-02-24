package project.adapter.in.web.Utils.Mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto.CreateMobileMoneyAccountDto;
import project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto.MomoTransactionHistoryDto;
import project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto.ReadMomoAccountDto;
import project.domain.model.MobileMoneyAccount;
import project.domain.model.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class MomoDtoMapper {
    @Inject
    BettingDTOMapper mapper;

    public ReadMomoAccountDto toMobileMoneyDto(MobileMoneyAccount acc) {
        var dto = new ReadMomoAccountDto();
        dto.setId(acc.getAccountId());
        dto.setAccountBalance(acc.getAccountBalance().getValue());
        dto.setAccountType(acc.accountType);
        dto.setDailyLimit(acc.getDailyLimit());
        dto.setWeeklyLimit(acc.getWeeklyLimit());
        dto.setMonthlyLimit(acc.getMonthlyLimit());
        return dto;
    }

    public List<ReadMomoAccountDto> toMobileMoneyDtos(List<MobileMoneyAccount> accounts) {
        return accounts.stream().map(this::toMobileMoneyDto).toList();
    }

    public MomoTransactionHistoryDto toMomotransactionHistory(List<Transaction> out) {
        return new MomoTransactionHistoryDto(new ArrayList<>(), out.stream().map(mapper::toTransactionDto).collect(Collectors.toCollection(ArrayList::new)));
    }

    public MobileMoneyAccount toMobileMoneyDomain(CreateMobileMoneyAccountDto dto) {
        Objects.requireNonNull(dto, "dto");
        Objects.requireNonNull(dto.getAccountType(), "accountType");
        return new MobileMoneyAccount(dto.getId(), dto.getAccountType(), dto.getName());
    }
}
