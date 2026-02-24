package project.adapter.in.web.BettingAccount.bettinAccountDTO;

import project.adapter.in.web.Utils.Link;

import java.util.List;

public record AllMatchEventPickDto(List<ReadMatchEventPickDto> outComes, List<Link> links) {
}
