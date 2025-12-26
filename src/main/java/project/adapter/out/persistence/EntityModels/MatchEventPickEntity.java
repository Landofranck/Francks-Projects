package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class MatchEventPickEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String matchKey;
    private String outcomeName;
    private double odd;
    @ManyToOne
    @JoinColumn(name = "parentBetSlipEntity_id")
    private BetSlipEntity parentBetSlipEntity;

    public MatchEventPickEntity(){}

    public MatchEventPickEntity(String matchKey, String outcomeName, double odd) {
        this.matchKey = Objects.requireNonNull(matchKey, "matchKey");
        this.outcomeName = Objects.requireNonNull(outcomeName, "outcomeName");
        this.odd = Objects.requireNonNull(odd, "odd");
    }


    public BetSlipEntity getParent() {
        return parentBetSlipEntity;
    }

    public void setParent(BetSlipEntity parent) {
        this.parentBetSlipEntity = parent;
    }

    public String matchKey() {
        return matchKey;
    }

    public String getMatchKey() {
        return matchKey;
    }

    public String outcomeName() {
        return outcomeName;
    }

    public String getOutcomeName() {
        return outcomeName;
    }

    public double odd() {
        return odd;
    }

    public double getOdd() {
        return odd;
    }

    public Long getId() {
        return id;
    }

    public void setMatchKey(String matchKey) {
        this.matchKey = matchKey;
    }

    public void setOdd(double odd) {
        this.odd = odd;
    }

    public void setOutcomeName(String outcomeName) {
        this.outcomeName = outcomeName;
    }
}
