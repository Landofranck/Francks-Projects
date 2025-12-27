package project.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class MatchEventPick {
    private final String matchKey;
    private final String outcomeName;
    private final double odd;
    private BetSlip owner;

    public MatchEventPick(String matchKey, String outcomeName, double odd) {
        this.matchKey = Objects.requireNonNull(matchKey, "matchKey");
        this.outcomeName = Objects.requireNonNull(outcomeName, "outcomeName");
        this.odd = Objects.requireNonNull(odd, "odd");
    }

    public void setOwner(BetSlip owner) {
        this.owner = owner;
    }

    public BetSlip getOwner() {
        return owner;
    }

    public String matchKey() { return matchKey; }

    public String getMatchKey() { return matchKey; }
    public String outcomeName() { return outcomeName; }

    public String getOutcomeName() { return outcomeName; }
    public double odd() { return odd; }

    public double getOdd() { return odd; }


}
