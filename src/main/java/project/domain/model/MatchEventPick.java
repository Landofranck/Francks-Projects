package project.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class MatchEventPick {
    private String matchKey;
    private final String outcomeName;
    private final double odd;
    private BetSlip owner;

    //when a pick is created we should only know the outcome  and odds first, match key and owner is added later
    public MatchEventPick(String outcomeName, double odd) {
        if(odd <=0)throw new IllegalArgumentException("odd must be > 0");
        this.outcomeName = outcomeName;
        this.odd = Objects.requireNonNull(odd, "odd");
    }

    public void setOwner(BetSlip owner) {
        this.owner = owner;
    }

    public BetSlip getOwner() {
        return owner;
    }


    public String getMatchKey() { return matchKey; }

    public String getOutcomeName() { return outcomeName; }
    public void setMatchKey(String matchKey) {
        this.matchKey = matchKey;
    }

    public double getOdd() { return odd; }


}
