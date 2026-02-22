package project.application.port.in.MomoAccounts;

import project.domain.model.Enums.TransactionType;
import project.domain.model.Transaction;

import java.util.List;

public interface LoadMomoTransactionHistoryUseCase {
    List<Transaction> loadTransactionHistory(Long momoId, TransactionType type);
}
