package project.adapter.in.web;


import java.util.List;

public class MatchDto {
    private Long matchId;
    private String home;
    private String away;
    private List<MatchEventPickDto> matchOutComes;

    public MatchDto() {}

    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }

    public String getHome() { return home; }
    public void setHome(String home) { this.home = home; }

    public String getAway() { return away; }
    public void setAway(String away) { this.away = away; }

    public List<MatchEventPickDto> getMatchOutComes() { return matchOutComes; }
    public void setMatchOutComes(List<MatchEventPickDto> matchOutComes) { this.matchOutComes = matchOutComes; }

}


