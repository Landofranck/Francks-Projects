package project.adapter.in.web.Utils.Mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto.CreateMobileMoneyAccountDto;
import project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto.MomoTransactionHistoryDto;
import project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto.ReadMomoAccountDto;
import project.domain.model.Enums.MomoAccountType;
import project.domain.model.MobileMoneyAccount;
import project.domain.model.Transaction;

import java.math.BigDecimal;
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
        dto.setDailyLimitAmount(acc.getDailyLimitAmount().getValue());
        dto.setWeeklyLimitAmount(acc.getWeeklyLimitAmount().getValue());
        dto.setMonthlyLimitAmount(acc.getMonthlyLimitAmount().getValue());
        dto.setId(acc.getAccountId());
        dto.setAccountBalance(acc.getAccountBalance().getValue());
        dto.setAccountType(acc.accountType);
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
        if (dto.getAccountType() == MomoAccountType.MTN)
            return new MobileMoneyAccount(dto.getId(), dto.getAccountType(), dto.getName(), BigDecimal.valueOf(2500000), BigDecimal.valueOf(10000000), BigDecimal.valueOf(20000000));
        else
            return new MobileMoneyAccount(dto.getId(), dto.getAccountType(), dto.getName(), BigDecimal.valueOf(2000000), BigDecimal.valueOf(5000000), BigDecimal.valueOf(10000000));

    }
}
