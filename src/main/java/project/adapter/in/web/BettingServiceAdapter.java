package project.adapter.in.web;

import jakarta.enterprise.context.ApplicationScoped;
import project.adapter.in.web.Reducer.UpdateMatchDto;
import project.adapter.in.web.TransactionDTO.WithdrawDto;
import project.adapter.in.web.bettinAccountDTO.AddPickRequestBetSlipDto;
import project.adapter.in.web.bettinAccountDTO.BettingAccountDto;
import project.adapter.in.web.bettinAccountDTO.BonusDto;
import project.adapter.in.web.bettinAccountDTO.CreateBettingAccountDto;
import project.adapter.in.web.MobileMoneyDto.CreateMobileMoneyAccountDto;
import project.adapter.in.web.MobileMoneyDto.MomoTopUpRequestDto;
import project.adapter.in.web.MobileMoneyDto.MomoTransferRequestDto;
import project.adapter.in.web.MobileMoneyDto.ReadMomoAccountDto;
import project.adapter.in.web.bettinAccountDTO.betslip.BetSlipDto;
import project.adapter.in.web.bettinAccountDTO.betslip.MakeBetRequestDto;
import project.application.port.in.*;
import jakarta.inject.Inject;
import project.application.port.in.BettingAccount.CreatNewBonusUseCase;
import project.application.port.in.BettingAccount.CreateBettingAccountUseCase;
import project.application.port.in.BettingAccount.LoadAllBettingAccountsUseCase;
import project.application.port.in.BettingAccount.LoadBettingAccountByIdUsecase;
import project.application.port.in.MomoAccounts.*;
import project.application.port.in.betSlip.AddEventPickToBetSlipUseCase;
import project.application.port.in.betSlip.CreateMatchUseCase;
import project.application.port.in.betSlip.MakeBetUseCase;
import project.domain.model.*;

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
    @Inject
    GetMatchByIdUseCase getMatchByIdUseCase;
    @Inject
    MakeWithdrawalUseCase makeWithdrawal;
    @Inject
    UpdateMatchUseCase updateMatchUse;
    @Inject
    CreatNewBonusUseCase creatNewBonus;

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
        if (dto.getMatchOutComes() == null || dto.getMatchOutComes().isEmpty())
            throw new IllegalArgumentException("you need outcomes BSA line 77");
        var domain = mapper.matchOutComePickList(dto);
        if (domain.getMatchOutComes() == null || domain.getMatchOutComes().isEmpty())
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
        return makeBetUseCase.makeBet(bettingAccountId, dto.getMatchIds(), dto.getOutComes(), new Money(dto.getStake()), dto.getStrategy(), dto.getBonusSlip());
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

    public void updateMatch(Long id, UpdateMatchDto dto) {
        var in = mapper.matchOutComePickList(dto);
        updateMatchUse.updateMatch(id, in);
    }

    public MatchDto getMatchByid(Long id) {
        return mapper.toMatchDto(getMatchByIdUseCase.getMatchById(id));
    }

    public void createBonus(Long bettingAccountId, BonusDto bonus) {
        Bonus in = mapper.toBonusDomain(bonus);
        creatNewBonus.createNewBonus(bettingAccountId, in);
    }

    public void withdrawFromBettingToMobileMoney(Long bettingId, Long momoId, WithdrawDto dto) {
        makeWithdrawal.withdrawFromBettingToMobileMoney(bettingId, momoId, dto.getAmount(), dto.getDescription());
    }
}
