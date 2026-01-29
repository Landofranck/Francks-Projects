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
import project.application.port.in.BettingAccount.CreateBettingAccountUseCase;
import project.application.port.in.BettingAccount.LoadAllBettingAccountsUseCase;
import project.application.port.in.BettingAccount.LoadBettingAccountByIdUsecase;
import project.application.port.in.MomoAccounts.*;
import project.application.port.in.Reducer.AddMatchToReducerUseCase;
import project.application.port.in.Reducer.CreateNewReducerUseCase;
import project.application.port.in.Reducer.LoadReducerByIdUseCase;
import project.application.port.in.betSlip.AddEventPickToBetSlipUseCase;
import project.application.port.in.betSlip.CreateEmptyBetSlipUseCase;
import project.application.port.in.betSlip.CreateMatchUseCase;
import project.application.port.in.betSlip.MakeBetUseCase;
import project.domain.model.BettingAccount;
import project.domain.model.DraftBetSlip;
import project.domain.model.Enums.BetCategory;
import project.domain.model.MobileMoneyAccount;
import project.domain.model.Money;

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
    MakeBetUseCase makeBetUseCase;
    @Inject
    LoadBettingAccountByIdUsecase loadAccount;
    @Inject
    LoadMomoAccountByIdUsecase loadMomoAccount;

    @Inject
    DeleteMatchByIdUsecase deleteMatchFromSystem;

    public Long createNewBettingAccount(CreateBettingAccountDto dto) {
        var domain = new BettingAccount(dto.getAccountName(), dto.getBrokerType());
        return createBettingAccountUseCase.createNewBettingAccount(domain);
    }

    public Long createNewMobileMoneyAccount(Long id, CreateMobileMoneyAccountDto dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Momo id is required");
        }
        var domain = new MobileMoneyAccount(id, dto.accountType);
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
        if (dto.getMatchOutComes().isEmpty() || dto.getMatchOutComes() == null)
            throw new IllegalArgumentException("you need outcomes BSA line 77");
        var domain = mapper.toMatchDomain(dto);
        if (domain.getMatchOutComes().isEmpty() || domain.getMatchOutComes() == null)
            throw new IllegalArgumentException("you need outcomes BSA line 77");

        return createMatchUse.createMatch(domain);

    }

    public void topUpMomo(Long id, MomoTopUpRequestDto dto) {
        topUpMomoUseCase.topUp(id, dto.amount, dto.description);
    }

    public List<MatchDto> getAllMatches() {
        var matches = loadAllMatches.getAllMatches();
        List<MatchDto> list = mapper.toMatchDtos(matches);
        return list;
    }

    public BetSlipDto addPickToBetSlip(Long bettingAccountId, AddPickRequestBetSlipDto dto) {
        DraftBetSlip updated = addEventPick.addPick(bettingAccountId, dto.getMatchId(), dto.getOutComeName());
        return mapper.toDraftSlipDto(updated);
    }

    public Long makeBet(Long bettingAccountId, MakeBetRequestDto dto) {
        return makeBetUseCase.makeBet(bettingAccountId, dto.getMatchIds(), dto.getOutComes(), new Money(dto.getStake()), dto.getDescription());
    }

    public BettingAccountDto loadBettingAccount(Long id) {
        var out = mapper.toBettingAccountDto(loadAccount.loadAccount(id));
        return out;
    }

    public ReadMomoAccountDto getMomoAccountById(Long momoId) {
        var out = mapper.toMobileMoneyDto(loadMomoAccount.loadMomoById(momoId));
        return out;
    }

    public void deleteMatchFromSystem(IdDto id) {
        deleteMatchFromSystem.deleteMatchById(id.Id());
    }
}
