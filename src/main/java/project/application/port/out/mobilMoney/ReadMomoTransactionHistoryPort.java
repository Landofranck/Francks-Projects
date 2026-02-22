package project.application.port.out.mobilMoney;

import project.domain.model.Enums.TransactionType;
import project.domain.model.Transaction;

import java.util.List;

public interface ReadMomoTransactionHistoryPort {
    List<Transaction> readMomoTransactions(Long momoId, TransactionType type);
}
