package project.adapter.in.web;

import jakarta.enterprise.context.ApplicationScoped;
import project.adapter.in.web.BettinAccountDTO.CreateBettingAccountDto;
import project.domain.model.BettingAccount;
import project.domain.model.MobileMoneyAccount;

import java.util.Objects;

@ApplicationScoped
public class DTOMapper {
    public BettingAccount toBettingAccountDomain(CreateBettingAccountDto dto) {
        Objects.requireNonNull(dto, "dto");
        Objects.requireNonNull(dto.getAccountName(), "accountName");
        Objects.requireNonNull(dto.getBrokerType(), "brokerType");

        return new BettingAccount(dto.getAccountName(), dto.getBrokerType());
    }

    public MobileMoneyAccount toMobileMoneyDomain(Long id,MobileMoneyAccountDto dto) {
        Objects.requireNonNull(dto, "dto");
        Objects.requireNonNull(dto.accountType, "accountType");
        return new MobileMoneyAccount(id, dto.accountType);
    }


}
