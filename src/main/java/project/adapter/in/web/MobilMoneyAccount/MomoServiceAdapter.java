package project.adapter.in.web.MobilMoneyAccount;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto.*;
import project.adapter.in.web.Utils.Mapper.MomoDtoMapper;
import project.application.port.in.MomoAccounts.*;
import project.domain.model.Enums.TransactionType;

import java.util.ArrayList;

@ApplicationScoped
public class MomoServiceAdapter {
    @Inject
    LoadAllMomoAccountsUseCase getAllMomoAccountsUseCase;
    @Inject
    TransferMomoUseCase transferMomoUseCase;
    @Inject
    CreateMobileMoneyAccountUseCase createMobileMoneyAccountUseCase;
    @Inject
    LoadMomoAccountByIdUsecase loadMomoAccount;
    @Inject
    TopUpMomoUseCase topUpMomoUseCase;

    @Inject
    LoadMomoTransactionHistoryUseCase loadMomoTransactionHistory;
    @Inject
    MomoDtoMapper momoMapper;

    public void createNewMobileMoneyAccount(CreateMobileMoneyAccountDto dto) {
        var domain = momoMapper.toMobileMoneyDomain(dto);
            createMobileMoneyAccountUseCase.createNewMobileMoneyAccount(domain);

    }

    public GetAllMomoDto getAllMomoAccounts() {
        var domains = getAllMomoAccountsUseCase.getAllMomoAccounts();
        return new GetAllMomoDto(momoMapper.toMobileMoneyDtos(domains), new ArrayList<>());
    }

    public void transferMomo(MomoTransferRequestDto dto) {
        transferMomoUseCase.transfer(dto.fromAccountId, dto.getTransactionTime(), dto.toAccountId, dto.amount, dto.description);
    }

    public void topUpMomo(Long id, MomoTopUpRequestDto dto) {
        topUpMomoUseCase.topUp(id, dto.getTransactionTime(), dto.amount, dto.description);
    }

    public ReadMomoAccountDto getMomoAccountById(Long momoId) {
        return momoMapper.toMobileMoneyDto(loadMomoAccount.loadMomoById(momoId));
    }

    public MomoTransactionHistoryDto getMomoTransactions(Long momoId, TransactionType type) {
        var out = loadMomoTransactionHistory.loadTransactionHistory(momoId, type);
        return momoMapper.toMomotransactionHistory(out);
    }
}
