package project.adapter.in.web.bettinAccountDTO;

import project.adapter.in.web.Utils.Link;

import java.util.List;

public record AllMatchesDto(List<MatchDto> allMatches, List<Link> links) {
}
