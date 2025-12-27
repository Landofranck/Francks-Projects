package project.adapter.in.web;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.adapter.in.web.BettinAccountDTO.BettingAccountDto;
import project.application.port.in.CreateBettingAccountUseCase;

@ApplicationScoped
public class BettingServiceAdapter {
    @Inject
    CreateBettingAccountUseCase createBettingAccountUseCase;

    @Inject
    DTOMapper mapper;

    public Long createNewBettingAccount(BettingAccountDto dto) {
        var domain = mapper.toBettingAccountDomain(dto);
        return createBettingAccountUseCase.createNewBettingAccount(domain);
    }
}
