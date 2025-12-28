package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String home;
    private String away;

    @OneToMany(mappedBy = "parentMatch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchOutcomeEntity> outcomes = new ArrayList<>();

    public void addOutcome(MatchOutcomeEntity o) {
        outcomes.add(o);
        o.setParentMatch(this);
    }

    public Long getId() {
        return id;
    }

    public String getHome() {
        return home;
    }

    public String getAway() {
        return away;
    }

    public List<MatchOutcomeEntity> getOutcomes() {
        return outcomes;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public void setAway(String away) {
        this.away = away;
    }
}
