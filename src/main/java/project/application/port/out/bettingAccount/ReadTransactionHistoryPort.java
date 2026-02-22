package project.application.port.out.bettingAccount;

import project.domain.model.Enums.TransactionType;
import project.domain.model.Transaction;

import java.util.List;

public interface ReadTransactionHistoryPort {
    List<Transaction> readTransactionHistory(Long bettingId, TransactionType type);
}
