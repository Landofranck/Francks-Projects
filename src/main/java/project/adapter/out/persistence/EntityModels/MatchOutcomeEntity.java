package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Optional;

@Entity
public class MatchOutcomeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String matchKey;
    private String outcomeName;
    private Double odd;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentMatch_id", nullable = false)
    private MatchEntity parentMatch;

    public MatchOutcomeEntity() {

    }

    public void setMatchKey(String matchKey) {
        this.matchKey = matchKey;
    }

    public String getMatchKey() {
        return matchKey;
    }

    public MatchEntity getParentMatch() {
        return parentMatch;
    }

    public void setParentMatch(MatchEntity parentMatch) {
        this.parentMatch = parentMatch;
    }

    public Long getId() {
        return id;
    }

    public void setOdd(Double odd) {
        this.odd = odd;
    }

    public void setOutcomeName(String outcomeName) {
        this.outcomeName = outcomeName;
    }

    public String getOutcomeName() {
        return outcomeName;
    }

    public Double getOdd() {
        return odd;
    }
}
