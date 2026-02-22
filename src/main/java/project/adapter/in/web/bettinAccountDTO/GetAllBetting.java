package project.adapter.in.web.bettinAccountDTO;

import project.adapter.in.web.MobileMoneyDto.ReadMomoAccountDto;
import project.adapter.in.web.Utils.Link;

import java.util.List;

public record GetAllBetting(
        List<GetBettingAccountDto> bettingAccounts,
        List<Link> links) {
}