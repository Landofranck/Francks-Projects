package project.adapter.in.web;

import jakarta.enterprise.context.ApplicationScoped;
import project.adapter.in.web.MobileMoneyDto.*;
import project.adapter.in.web.Reducer.UpdateMatchDto;
import project.adapter.in.web.TransactionDTO.WithdrawDto;
import project.adapter.in.web.Utils.DTOMapper;
import project.adapter.in.web.Utils.IdDto;
import project.adapter.in.web.bettinAccountDTO.*;
import project.adapter.in.web.bettinAccountDTO.betslip.*;
import project.application.port.in.*;
import jakarta.inject.Inject;
import project.application.port.in.BettingAccount.*;
import project.application.port.in.MomoAccounts.*;
import project.application.port.in.betSlip.*;
import project.domain.model.*;
import project.domain.model.Enums.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Collectors;

@ApplicationScoped
public class BettingServiceAdapter {
    @Inject
    CreateBettingAccountUseCase createBettingAccountUseCase;
    @Inject
    LoadAllBettingAccountsUseCase getAllBettingAccountsUseCase;
    @Inject
    LoadBetHistoryUseCase getBetHistory;
    @Inject
    LoadTransactionHistoryUseCase getTransactionHistory;
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
    LoadBettingSummaryOfBettingAccountUseCase loadAccount;
    @Inject
    LoadMomoAccountByIdUsecase loadMomoAccount;
    @Inject
    GetBetSlipByIdsecase getBetSlipById;
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
    @Inject
    FindMatchOutComeByParametersUseCases findMatchOutComeByParameters;
    @Inject
    UpdateMatchPickStatusUsecase updateStatus;
    @Inject
    SetBetSlipToRefundUseCase refundUseCase;
    @Inject
    LoadMomoTransactionHistoryUseCase loadMomoTransactionHistory;
    @Inject
    CreateEmptyBetSlipUseCase createEmptyBetSlip;
    @Inject
    RemovePickByNumberUseCase removePickByNumberUseCase;
    @Inject
    LoadDraftBetSlipByParentUseCase getDraft;

    public void createNewBettingAccount(CreateBettingAccountDto dto) {
        var domain = mapper.toBettingAccountDomain(dto);
        createBettingAccountUseCase.createNewBettingAccount(domain);
    }

    public void creatEmptyDraftSlip(Long bettingId) {
        createEmptyBetSlip.createEmpty(bettingId, BetCategory.SINGLE);
    }

    public ReadBetSlipDto getBetSlip(Long id) {
        return mapper.toBetSlipDto(getBetSlipById.getBetSlip(id));
    }

    public void createNewMobileMoneyAccount(CreateMobileMoneyAccountDto dto) {
        var domain = mapper.toMobileMoneyDomain(dto);
        createMobileMoneyAccountUseCase.createNewMobileMoneyAccount(domain);
    }

    public GetAllBetting getAllBettingAccounts(BrokerType broker) {
        var domains = getAllBettingAccountsUseCase.loadAllBettingAccounts(broker);
        return new GetAllBetting(mapper.toBettingAccountDtos(domains), new ArrayList<>());
    }

    public GetAllMomoDto getAllMomoAccounts() {
        var domains = getAllMomoAccountsUseCase.getAllMomoAccounts();
        return new GetAllMomoDto(mapper.toMobileMoneyDtos(domains), new ArrayList<>());
    }

    public void transferMomo(MomoTransferRequestDto dto) {
        transferMomoUseCase.transfer(dto.fromAccountId, dto.toAccountId, dto.amount, dto.description);
    }

    public Long createMatch(MatchDto dto) {
        var domain = mapper.toMatchDomain(dto);

        return createMatchUse.createMatch(domain);

    }

    public void topUpMomo(Long id, MomoTopUpRequestDto dto) {
        topUpMomoUseCase.topUp(id, dto.amount, dto.description);
    }

    public AllMatchesDto getAllMatches(BrokerType broker, Long id,String name,Instant start,Instant end) {
        var matches = loadAllMatches.getAllMatches(broker,id,name,start,end);
        return new AllMatchesDto(mapper.toMatchDtos(matches),new ArrayList<>());
    }

    public void addPickToBetSlip(Long bettingAccountId, AddPickRequestBetSlipDto dto) {
        DraftBetSlip updated = addEventPick.addPick(bettingAccountId, dto.getMatchId(), dto.getOutComeName());
    }

    public void removePickByNumber(Long bettingId, int slipIndex) {
        removePickByNumberUseCase.removeSpecifiedPick(bettingId, slipIndex);
    }

    public void makeBet(Long bettingAccountId, MakeBetRequestDto dto) {
        makeBetUseCase.makeBet(bettingAccountId, dto.getMatchIds(), dto.getOutComes(), new Money(dto.getStake()), dto.getStrategy(), dto.getBonusSlip());
    }

    public GetBettingAccountDto loadBettingAccount(Long id) {
        var out = mapper.toGetBettingAccountDto(loadAccount.laadBettingAccount(id));
        return out;
    }

    public DraftDto getDraftSlip(Long bettingId) {
        return mapper.toDraftSlipDto(getDraft.loadSlipFromParents(bettingId));
    }

    public BetHistoryDto getBetHistory(Long bettingId, BetStatus status, String matchKey, BetStrategy strategy) {
        var history = getBetHistory.loadBetHistory(bettingId, status, matchKey, strategy);
        return mapper.toBetHistoryDto(history);
    }

    public TransactionHistoryDto getTransactionHistory(Long bettingId, TransactionType type) {
        var history = getTransactionHistory.loadTransactionHistory(bettingId, type);
        return mapper.toTransactionHistoryDto(history);
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

    public MatchDto getMatchByid(Long id, BrokerType broker,String name) {
        return mapper.toMatchDto(getMatchByIdUseCase.getMatchByIdOrName(id, broker,name));
    }

    public void createBonus(Long bettingAccountId, BonusDto bonus) {
        Bonus in = mapper.toBonusDomain(bonus);
        creatNewBonus.createNewBonus(bettingAccountId, in);
    }

    public void withdrawFromBettingToMobileMoney(Long bettingId, Long momoId, WithdrawDto dto) {
        makeWithdrawal.withdrawFromBettingToMobileMoney(bettingId, momoId, dto.getAmount(), dto.getDescription());
    }

    public AllMatchEventPickDto getMatchOutcomesByParam(String matchKey, String outComeName, League league) {
        var matchOutComes = findMatchOutComeByParameters.findMatches(matchKey, outComeName, league);
        return new AllMatchEventPickDto(matchOutComes.stream().map(mapper::toMatchEventPickDto).collect(Collectors.toCollection(ArrayList::new)),new ArrayList<>());
    }

    public void updateMatchOutcomes(UpdateMatchOutcomeDto dto) {
        var in = new MatchOutComePick(null, dto.matchKey(), dto.outComeName(), 1, dto.league());
        in.setOutcomePickStatus(dto.status());
        updateStatus.updateMatchPickStatus(in);
    }

    public void setSlipToRefund(Long betAccountId, Long slipId) {
        refundUseCase.setSlipToRefund(betAccountId, slipId);
    }

    public MomoTransactionHistoryDto getMomoTransactions(Long momoId, TransactionType type) {
        var out = loadMomoTransactionHistory.loadTransactionHistory(momoId, type);
        return mapper.toMomotransactionHistory(out);
    }
}
