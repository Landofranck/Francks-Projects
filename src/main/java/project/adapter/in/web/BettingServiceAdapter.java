package project.adapter.in.web;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import project.adapter.in.web.BettinAccountDTO.BettingAccountDto;
import project.adapter.in.web.BettinAccountDTO.CreateBettingAccountDto;
import project.adapter.in.web.MobileMoneyDto.MobileMoneyAccountDto;
import project.adapter.in.web.MobileMoneyDto.MomoTopUpRequestDto;
import project.adapter.in.web.MobileMoneyDto.MomoTransferRequestDto;
import project.application.port.in.*;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class BettingServiceAdapter {
    @Inject
    CreateBettingAccountUseCase createBettingAccountUseCase;
    @Inject
    LoadAllBettingAccountsUseCase getAllBettingAccountsUseCase;
    @Inject
    LoadAllMomoAccountsUseCase getAllMomoAccountsUseCase;
    @Inject
    TransferMomoUseCase transferMomoUseCase;

    @Inject
    CreateMobileMoneyAccountUseCase createMobileMoneyAccountUseCase;
    @Inject
    DTOMapper mapper;

    @Inject TopUpMomoUseCase topUpMomoUseCase;



    public Long createNewBettingAccount(CreateBettingAccountDto dto) {
        var domain = mapper.toBettingAccountDomain(dto);
        return createBettingAccountUseCase.createNewBettingAccount(domain);
    }

    public Long createNewMobileMoneyAccount(Long id, MobileMoneyAccountDto dto) {
        if (dto.getId()==null){
            throw new WebApplicationException("Momo id is required", 400);
        }
        var domain = mapper.toMobileMoneyDomain(id, dto);
        return createMobileMoneyAccountUseCase.createNewMobileMoneyAccount(domain);
    }

    public List<BettingAccountDto> getAllBettingAccounts() {
        var domains = getAllBettingAccountsUseCase.loadAllBettingAccounts();
        return mapper.toBettingAccountDtos(domains);
    }

    public List<MobileMoneyAccountDto> getAllMomoAccounts() {
        var domains = getAllMomoAccountsUseCase.getAllMomoAccounts();
        return mapper.toMobileMoneyDtos(domains);
    }

    public void transferMomo(MomoTransferRequestDto dto) {
        transferMomoUseCase.transfer(dto.fromAccountId, dto.toAccountId, dto.amount);
    }


    public void topUpMomo(Long id, MomoTopUpRequestDto dto) {
        topUpMomoUseCase.topUp(id, dto.amount);
    }
}
