package project.domain.model;


import java.util.Objects;

public class MatchEventPick {
    private Long identity;
    private String matchKey;
    private final String outcomeName;
    private final double odd;
    private Event owner;

    //when a pick is created we should only know the outcome  and odds first, match key and owner is added later
    public MatchEventPick(Long id,String matchKey, String outcomeName, double odd) {
        if(odd <=0)throw new IllegalArgumentException("odd must be > 0");
        this.outcomeName = outcomeName;
        this.odd = Objects.requireNonNull(odd, "odd");
        this.matchKey=matchKey;
        this.identity =id;
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

    public double getOdd() {
        return odd;
    }

    public Event getOwner() {
        return owner;
    }

    public void setOwner(Event owner) {
        this.owner = owner;
    }

    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    public Long getIdentity() {
        return identity;
    }
}
