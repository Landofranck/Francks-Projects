package project.adapter.in.web.BettingAccount.bettinAccountDTO;

import project.adapter.in.web.Utils.Link;

import java.util.List;

public record GetAllBetting(
        List<GetBettingAccountDto> bettingAccounts,
        List<Link> links) {
}