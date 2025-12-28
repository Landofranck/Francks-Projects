package project.adapter.in.web.TransactionDTO;

import jakarta.validation.constraints.NotNull;
import project.domain.model.Enums.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;

public class TransactionDto {
    private Long id;
    @NotNull(message = "accountType")
    private TransactionType type;
    @NotNull(message = "accountType")

    private BigDecimal transactionAmount;
    @NotNull(message = "accountType")

    private BigDecimal balanceAfter;
    @NotNull(message = "accountType")

    private Instant createdAt;



    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public BigDecimal getTransactionAmount() { return transactionAmount; }
    public void setTransactionAmount(BigDecimal transactionAmount) { this.transactionAmount = transactionAmount; }

    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(BigDecimal balanceAfter) { this.balanceAfter = balanceAfter; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
