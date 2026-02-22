package project.application.port.in.BettingAccount;

import project.domain.model.Enums.TransactionType;
import project.domain.model.Transaction;

import java.util.List;

public interface LoadTransactionHistoryUseCase {
    List<Transaction> loadTransactionHistory(Long bettingId, TransactionType type);
}
