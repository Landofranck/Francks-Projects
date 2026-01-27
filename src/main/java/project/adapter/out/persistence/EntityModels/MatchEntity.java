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
    private List<MatchOutcomeEntity> matchOutComes = new ArrayList<>();

    //this line indicates that a match can also belong to a reducer
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "matchReducerParent_id", nullable = false)
    private ReducerEntity matchReducerParent;

    public MatchEntity() {
    }

    public void addOutcome(MatchOutcomeEntity o) {
        matchOutComes.add(o);
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
        return matchOutComes;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public ReducerEntity getMatchReducerParent() {
        return matchReducerParent;
    }

    public void setMatchReducerParent(ReducerEntity matchReducerParent) {
        this.matchReducerParent = matchReducerParent;
    }
}
