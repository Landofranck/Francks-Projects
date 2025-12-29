package project.adapter.in.web.bettinAccountDTO.betslip;


import jakarta.validation.constraints.NotNull;
import project.adapter.in.web.MatchDto;

import java.math.BigDecimal;
import java.util.List;

public class CreateBetSlipsDto {
    @NotNull
    private String category;

    public CreateBetSlipsDto() {}

    public String getCategory() { return category; }
}

