package project.adapter.in.web;

import project.domain.model.BetSlip;

public class MatchEventPickDto {
    private String matchKey;
    private  String outcomeName;
    private  double odd;
    private BetSlipDto owner;

    public void setMatchKey(String matchKey) {
        this.matchKey = matchKey;
    }

    public void setOwner(BetSlipDto owner) {
        this.owner = owner;
    }

    public void setOdd(double odd) {
        this.odd = odd;
    }

    public void setOutcomeName(String outcomeName) {
        this.outcomeName = outcomeName;
    }

    public BetSlipDto getOwner() {
        return owner;
    }

    public double getOdd() {
        return odd;
    }

    public String getMatchKey() {
        return matchKey;
    }

    public String getOutcomeName() {
        return outcomeName;
    }
}
