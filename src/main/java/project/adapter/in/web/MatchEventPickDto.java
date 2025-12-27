package project.adapter.in.web;


public class MatchEventPickDto {
    private String matchKey;
    private String outcomeName;
    private double odd;

    public MatchEventPickDto() {}

    public String getMatchKey() { return matchKey; }
    public void setMatchKey(String matchKey) { this.matchKey = matchKey; }

    public String getOutcomeName() { return outcomeName; }
    public void setOutcomeName(String outcomeName) { this.outcomeName = outcomeName; }

    public double getOdd() { return odd; }
    public void setOdd(double odd) { this.odd = odd; }
}
