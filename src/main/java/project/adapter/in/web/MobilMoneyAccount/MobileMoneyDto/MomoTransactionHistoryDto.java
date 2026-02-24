package project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto;

import project.adapter.in.web.TransactionDTO.TransactionDto;
import project.adapter.in.web.Utils.Link;

import java.util.List;

public record MomoTransactionHistoryDto(List<Link> links, List<TransactionDto> history) {
}
