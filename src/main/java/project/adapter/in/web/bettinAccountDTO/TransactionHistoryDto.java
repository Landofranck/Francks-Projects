package project.adapter.in.web.bettinAccountDTO;

import jakarta.validation.constraints.NotNull;
import project.adapter.in.web.TransactionDTO.TransactionDto;
import project.adapter.in.web.Utils.Link;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryDto {
    @NotNull
    private List<TransactionDto> transactionHistory;
    private List<Link> links;


    public TransactionHistoryDto(List<TransactionDto> transactionHistory) {
        this.transactionHistory = transactionHistory;
        this.links=new ArrayList<>();
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<TransactionDto> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(List<TransactionDto> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }
}
