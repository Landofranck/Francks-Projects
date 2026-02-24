package project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import project.domain.model.Enums.MomoAccountType;


public class CreateMobileMoneyAccountDto {
    @NotNull
    @PositiveOrZero(message = "you must put momo id momodto 8")
    private Long id;
    @NotNull(message = "the account must have a type momo dto 10")
    private MomoAccountType accountType;
    @NotEmpty(message = "the account must have a name momo dto 12")
    private String name;

    public void setId(Long id) {
        this.id = id;
    }



    public Long getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @NotNull(message = "the account must have a type momo dto 10") MomoAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(MomoAccountType accountType) {
        this.accountType = accountType;
    }
}
