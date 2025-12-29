package project.adapter.in.web.bettinAccountDTO;

public class AddPickRequestBetSlipDto {
    private Long matchId;
    private String outComeName;

    public String getOutComeName() {
        return outComeName;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setOutComeName(String outComeName) {
        this.outComeName = outComeName;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

}