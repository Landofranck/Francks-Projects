package project.adapter.out.persistence.EntityModels.BettingAccount;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MatchEventPickEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String matchKey;
    private String outcomeName;
    private double odd;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentBetSlipEntity_id",nullable = true)
    private BetSlipEntity parentBetSlipEntity;

    public MatchEventPickEntity(){}




    public BetSlipEntity getParent() {
        return parentBetSlipEntity;
    }

    public void setParent(BetSlipEntity parent) {
        this.parentBetSlipEntity = parent;
    }

    public String matchKey() {
        return matchKey;
    }
}
