package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;
import project.adapter.out.persistence.Embeddables.BlockEmb;
import project.adapter.out.persistence.EntityModels.BettingAccount.MatchEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ReducerSummaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reducerSummaryId;
    private BigDecimal stake;
    private BigDecimal totalStaked;
    private BigDecimal lossOrGain;
    @OneToMany(mappedBy = "parentReducerSummaryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReducerEntity> reducerEntities;
    @ElementCollection
    private List<BlockEmb> blocks = new ArrayList<>();
    private String description;

    public ReducerSummaryEntity() {
    }

    public void addReducerEntity(ReducerEntity reducerEntity) {
        reducerEntity.setParentReducerSummaryEntity(this);
        this.reducerEntities.add(reducerEntity);
    }

    public Long getReducerSummaryId() {
        return reducerSummaryId;
    }

    public void setReducerSummaryId(Long reducerSummaryId) {
        this.reducerSummaryId = reducerSummaryId;
    }

    public BigDecimal getStake() {
        return stake;
    }

    public void setStake(BigDecimal stake) {
        this.stake = stake;
    }

    public BigDecimal getTotalStaked() {
        return totalStaked;
    }

    public void setTotalStaked(BigDecimal totalStaked) {
        this.totalStaked = totalStaked;
    }

    public BigDecimal getLossOrGain() {
        return lossOrGain;
    }

    public void setLossOrGain(BigDecimal lossOrGain) {
        this.lossOrGain = lossOrGain;
    }

    public List<ReducerEntity> getReducerEntities() {
        return reducerEntities;
    }

    public void setReducerEntities(List<ReducerEntity> reducerEntities) {
        this.reducerEntities = reducerEntities;
    }



    public List<BlockEmb> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BlockEmb> blocks) {
        this.blocks = blocks;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
