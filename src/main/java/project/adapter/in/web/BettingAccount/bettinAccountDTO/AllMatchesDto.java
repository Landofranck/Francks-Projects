package project.adapter.in.web.BettingAccount.bettinAccountDTO;

import project.adapter.in.web.Utils.Link;

import java.util.List;

public record AllMatchesDto(List<ReadMatchDto> allMatches, List<Link> links) {
}
