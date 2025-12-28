package project.adapter.in.web;

import jakarta.enterprise.context.ApplicationScoped;
import project.adapter.in.web.bettinAccountDTO.AddPickRequestBetSlipDto;
import project.adapter.in.web.bettinAccountDTO.BettingAccountDto;
import project.adapter.in.web.bettinAccountDTO.CreateBettingAccountDto;
import project.adapter.in.web.MobileMoneyDto.CreateMobileMoneyAccountDto;
import project.adapter.in.web.MobileMoneyDto.MomoTopUpRequestDto;
import project.adapter.in.web.MobileMoneyDto.MomoTransferRequestDto;
import project.adapter.in.web.MobileMoneyDto.ReadMomoAccountDto;
import project.adapter.in.web.bettinAccountDTO.betslip.BetSlipDto;
import project.adapter.in.web.bettinAccountDTO.betslip.MakeBetRequestDto;
import project.application.port.in.*;
import jakarta.inject.Inject;
import project.application.port.in.betSlip.AddEventPickToBetSlipUseCase;
import project.application.port.in.betSlip.CreateEmptyBetSlipUseCase;
import project.application.port.in.betSlip.CreateMatchUseCase;
import project.application.port.in.betSlip.MakeBetUseCase;

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
    @Inject
    TopUpMomoUseCase topUpMomoUseCase;
    @Inject
    CreateMatchUseCase createMatchUse;
    @Inject
    LoadAllMatchesUseCase loadAllMatches;
    @Inject
    AddEventPickToBetSlipUseCase addEventPick;
    @Inject
    CreateEmptyBetSlipUseCase createEmptyBetSlipUseCase;
    @Inject
    MakeBetUseCase makeBetUseCase;

    public Long createNewBettingAccount(CreateBettingAccountDto dto) {
        var domain = mapper.toBettingAccountDomain(dto);
        return createBettingAccountUseCase.createNewBettingAccount(domain);
    }

    public Long createNewMobileMoneyAccount(Long id, CreateMobileMoneyAccountDto dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Momo id is required");
        }
        var domain = mapper.toMobileMoneyDomain(id, dto);
        return createMobileMoneyAccountUseCase.createNewMobileMoneyAccount(domain);
    }

    public List<BettingAccountDto> getAllBettingAccounts() {
        var domains = getAllBettingAccountsUseCase.loadAllBettingAccounts();
        return mapper.toBettingAccountDtos(domains);
    }

    public List<ReadMomoAccountDto> getAllMomoAccounts() {
        var domains = getAllMomoAccountsUseCase.getAllMomoAccounts();
        return mapper.toMobileMoneyDtos(domains);
    }

    public void transferMomo(MomoTransferRequestDto dto) {
        transferMomoUseCase.transfer(dto.fromAccountId, dto.toAccountId, dto.amount, dto.description);
    }

    public Long createMatch(MatchDto dto) {
        if(dto.getMatchOutComes().isEmpty()||dto.getMatchOutComes()==null) throw new IllegalArgumentException("you need outcomes BSA line 77");
        var domain = mapper.toMatchDomain(dto);
        if(domain.getMatchOutComes().isEmpty()||domain.getMatchOutComes()==null) throw new IllegalArgumentException("you need outcomes BSA line 77");

        return createMatchUse.createMatch(domain);

    }

    public void topUpMomo(Long id, MomoTopUpRequestDto dto) {
        topUpMomoUseCase.topUp(id, dto.amount, dto.description);
    }

    public List<MatchDto> getAllMatches() {
        var matches=loadAllMatches.getAllMatches();
        return mapper.toMatchDtos(matches);
    }

    public BetSlipDto createEmptySlip(Long bettingAccountId, String category) {
        var slip = createEmptyBetSlipUseCase.createEmpty(bettingAccountId, category);
        return mapper.toBetSlipDto(slip);
    }

    public BetSlipDto addPickToBetSlip(Long bettingAccountId, AddPickRequestBetSlipDto dto) {
        var slipDomain = mapper.toBetSlipDomain(dto.getSlip());
        var updated = addEventPick.addPick(bettingAccountId, slipDomain, dto.getMatchId(), dto.getOutcomeName());
        return mapper.toBetSlipDto(updated);
    }

    public Long makeBet(Long bettingAccountId, MakeBetRequestDto dto) {
        var slipDomain = mapper.toBetSlipDomain(dto.getSlip());
        return makeBetUseCase.makeBet(bettingAccountId, slipDomain, dto.getStake(), dto.getDescription());
    }

}
