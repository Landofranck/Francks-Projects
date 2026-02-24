package project.adapter.in.web.BettingAccount.bettinAccountDTO.betslip;


import jakarta.validation.constraints.NotNull;

public class CreateBetSlipsDto {
    @NotNull
    private String category;

    public CreateBetSlipsDto() {}

    public String getCategory() { return category; }
}

