package project.adapter.in.web;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import project.domain.model.Enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

@XmlRootElement
public class TransactionDto {
    private long id;
    @NotNull(message = "transaction ammount must have a value")
    private BigDecimal transactionAmmount;

    @NotNull(message = "accountbalancetransaction ammount must have a value")
    private BigDecimal accountBalanceAfterTransaction;
    private Instant createdAt;
    @NotNull(message = "you must provide a transactiontype")
    private TransactionType type;
    private AccountDto owner;

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAccountBalanceAfterTransaction(BigDecimal accountBalanceAfterTransaction) {
        this.accountBalanceAfterTransaction = accountBalanceAfterTransaction;
    }

    public void setOwner(AccountDto owner) {
        this.owner = owner;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setTransactionAmmount(BigDecimal transactionAmmount) {
        this.transactionAmmount = transactionAmmount;
    }

    public long getId() {
        return this.id;
    }

}
