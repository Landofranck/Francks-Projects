package project.adapter.in.web;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import project.adapter.in.web.TransactionDTO.TransactionDto;
import project.domain.model.Enums.AccountType;

import java.math.BigDecimal;
import java.util.List;

@XmlRootElement
public class MobileMoneyAccountDto implements AccountDto{
    @NotNull(message = "you must put momo id")
    private Long id;
    @NotNull(message = "the account must have a type")
    public  AccountType accountType;
    @NotNull(message = "the account must have a balance")


    public void setId(Long id) {
        this.id = id;
    }



    @Override
    public Long getId() {
        return this.id;
    }
}
