package project.adapter.in.web;

import project.adapter.in.web.BettinAccountDTO.BettingAccountDto;
import project.domain.model.BettingAccount;
import project.domain.model.MobileMoneyAccount;

import java.util.Objects;

public class DTOMapper {
    public BettingAccount toBettingAccountDomain(BettingAccountDto dto) {
        Objects.requireNonNull(dto, "dto");
        Objects.requireNonNull(dto.getAccountName(), "accountName");
        Objects.requireNonNull(dto.getBrokerType(), "brokerType");

        return new BettingAccount(dto.getAccountName(), dto.getBrokerType());
    }

    public MobileMoneyAccount toMobileMoneyDomain(MobileMoneyAccountDto dto) {
        Objects.requireNonNull(dto, "dto");
        Objects.requireNonNull(dto.accountType, "accountType");
        return new MobileMoneyAccount(dto.accountType);
    }


}
