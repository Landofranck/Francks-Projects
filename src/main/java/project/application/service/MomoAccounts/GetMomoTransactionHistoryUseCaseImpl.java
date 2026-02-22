package project.application.service.MomoAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.MomoAccounts.LoadMomoTransactionHistoryUseCase;
import project.application.port.out.mobilMoney.ReadMomoTransactionHistoryPort;
import project.domain.model.Enums.TransactionType;
import project.domain.model.Transaction;

import java.util.List;

@ApplicationScoped
public class GetMomoTransactionHistoryUseCaseImpl implements LoadMomoTransactionHistoryUseCase {
    @Inject
    ReadMomoTransactionHistoryPort readMomoTransactionHistory;
    @Override
    public List<Transaction> loadTransactionHistory(Long momoId, TransactionType type) {
        return readMomoTransactionHistory.readMomoTransactions(momoId,type);
    }
}
