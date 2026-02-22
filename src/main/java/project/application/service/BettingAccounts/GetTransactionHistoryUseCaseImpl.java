package project.application.service.BettingAccounts;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.BettingAccount.LoadTransactionHistoryUseCase;
import project.application.port.out.bettingAccount.ReadTransactionHistoryPort;
import project.domain.model.Enums.TransactionType;
import project.domain.model.Transaction;
import java.util.List;

@ApplicationScoped
public class GetTransactionHistoryUseCaseImpl implements LoadTransactionHistoryUseCase {
    @Inject
    ReadTransactionHistoryPort readTransactionHistory;

    @Override
    public List<Transaction> loadTransactionHistory(Long bettingId, TransactionType type) {
        return readTransactionHistory.readTransactionHistory(bettingId, type);
    }
}
