package project.adapter.in.web.TransactionDTO;

import jakarta.validation.constraints.NotNull;
import project.adapter.in.web.Utils.Link;
import project.domain.model.Enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TransactionDto {
    @NotNull
    private Long id;
    @NotNull(message = "accountType")
    private TransactionType type;
    @NotNull(message = "accountType")

    private BigDecimal transactionAmount;
    @NotNull(message = "accountType")

    private BigDecimal balanceAfter;

    @NotNull(message = "accountType")
    private Instant createdAt;
    private Long betSlipId;
    private String description;
    private List<Link> links=new ArrayList<>();

    private Long ownerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setBetSlipId(Long betSlipId) {
        this.betSlipId = betSlipId;
    }

    public Long getBetSlipId() {
        return betSlipId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerId() {
        return ownerId;
    }
}
