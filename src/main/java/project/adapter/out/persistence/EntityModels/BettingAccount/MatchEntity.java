package project.adapter.out.persistence.EntityModels.BettingAccount;

import jakarta.persistence.*;
import project.adapter.out.persistence.EntityModels.MatchOutcomeEntity;
import project.adapter.out.persistence.EntityModels.ReducerEntity;
import project.domain.model.Enums.BrokerType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    private BrokerType broker;
    private String home;
    private String away;

    @OneToMany(mappedBy = "parentMatch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchOutcomeEntity> matchOutComes = new ArrayList<>();

    //this line indicates that a match can also belong to a reducer
    @ManyToMany(mappedBy = "betMatchEntities")
    private Set<ReducerEntity> reducers = new HashSet<>();

    public MatchEntity() {
    }

    public void addParent(ReducerEntity i) {
        this.reducers.add(i);
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

    public List<MatchOutcomeEntity> getMatchOutComes() {
        return matchOutComes;
    }

    public Set<ReducerEntity> getReducers() {
        return reducers;
    }

    public void setMatchOutComes(List<MatchOutcomeEntity> matchOutComes) {
        this.matchOutComes = matchOutComes;
    }

    public void setReducers(Set<ReducerEntity> reducers) {
        this.reducers = reducers;
    }

    public void setBroker(BrokerType broker) {
        this.broker = broker;
    }

    public BrokerType getBroker() {
        return broker;
    }
}
