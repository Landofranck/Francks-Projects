package project.adapter.in.web;

import jakarta.enterprise.context.ApplicationScoped;
import project.adapter.in.web.BettinAccountDTO.CreateBettingAccountDto;
import project.application.port.in.CreateMobileMoneyAccountUseCase;
import jakarta.inject.Inject;
import project.adapter.in.web.BettinAccountDTO.BettingAccountDto;
import project.application.port.in.CreateBettingAccountUseCase;

@ApplicationScoped
public class BettingServiceAdapter {
    @Inject
    CreateBettingAccountUseCase createBettingAccountUseCase;

    @Inject
    DTOMapper mapper;

    public Long createNewBettingAccount(CreateBettingAccountDto dto) {
        var domain = mapper.toBettingAccountDomain(dto);
        return createBettingAccountUseCase.createNewBettingAccount(domain);
    }

    @Inject
    CreateMobileMoneyAccountUseCase createMobileMoneyAccountUseCase;

    public Long createNewMobileMoneyAccount(Long id,MobileMoneyAccountDto dto) {
        var domain = mapper.toMobileMoneyDomain(id , dto);
        return createMobileMoneyAccountUseCase.createNewMobileMoneyAccount(domain);
    }
}
