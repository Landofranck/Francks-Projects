package project.adapter.out.persistence.EntityModels.MomoEntites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.adapter.out.persistence.EntityModels.BettingAccount.BetSlipEntity;

@Getter
@Setter
@Embeddable
public class MatchEventPickEmbd {
    private String matchKey;
    private String outcomeName;
    private double odd;
    public MatchEventPickEmbd(String matchKey,String outcomeName,double odd){
        this.matchKey=matchKey;
        this.outcomeName=outcomeName;
        this.odd=odd;
    }
}
