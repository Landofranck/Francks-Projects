package project.adapter.out.persistence.EntityModels.MomoEntites;

import jakarta.persistence.*;


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

    public MatchEventPickEmbd() {
    }

    public String getMatchKey() {
        return matchKey;
    }

    public void setMatchKey(String matchKey) {
        this.matchKey = matchKey;
    }

    public String getOutcomeName() {
        return outcomeName;
    }

    public void setOutcomeName(String outcomeName) {
        this.outcomeName = outcomeName;
    }

    public double getOdd() {
        return odd;
    }

    public void setOdd(double odd) {
        this.odd = odd;
    }
}
