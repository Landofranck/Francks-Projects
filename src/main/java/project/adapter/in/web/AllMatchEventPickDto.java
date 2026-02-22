package project.adapter.in.web;

import project.adapter.in.web.Utils.Link;
import project.adapter.in.web.bettinAccountDTO.MatchEventPickDto;

import java.util.List;

public record AllMatchEventPickDto(List<MatchEventPickDto> outComes, List<Link> links) {
}
