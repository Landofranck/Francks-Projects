package project.adapter.in.web.MobileMoneyDto;

import jakarta.validation.constraints.NotNull;
import project.domain.model.Enums.MomoAccountType;


public class CreateMobileMoneyAccountDto {
    @NotNull(message = "you must put momo id momodto 8")
    private Long id;
    @NotNull(message = "the account must have a type momo dto 10")
    public MomoAccountType accountType;

    public void setId(Long id) {
        this.id = id;
    }



    public Long getId() {
        return this.id;
    }


    public void setAccountType(MomoAccountType accountType) {
        this.accountType = accountType;
    }
}
