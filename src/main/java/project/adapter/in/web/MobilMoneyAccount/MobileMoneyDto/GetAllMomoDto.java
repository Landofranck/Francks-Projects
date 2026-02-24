package project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto;

import project.adapter.in.web.Utils.Link;

import java.util.List;

public record GetAllMomoDto(
        List<ReadMomoAccountDto> allMomos,
        List<Link> links) {
}
