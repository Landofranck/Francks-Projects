package project.adapter.in.web.Reducer.ReducerDto;

import project.adapter.in.web.BettingAccount.bettinAccountDTO.ReadMatchDto;
import project.adapter.in.web.Reducer.ReducerSlipDto;
import project.adapter.in.web.Utils.Link;
import project.domain.model.Match;
import project.domain.model.Money;
import project.domain.model.Reducer.Block;
import project.domain.model.Reducer.Reducer;
import project.domain.model.Reducer.ReducerBetSlip;
import project.domain.model.Reducer.Shuffle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReadReducerSummaryDto {
    private Long reducerSummaryId;
    private String description;
    private List<Shuffle> shuffleCombinations;
    private BigDecimal stake;
    private BigDecimal totalStaked;
    private BigDecimal lossOrGain;
    private List<BlockDto> blockDtos;
    private List<ReadReducerDto> reducerDtos;
    private List<ReducerSlipDto> reducerBetSlipDtos;
    private List<ReadMatchDto> matchDtos;
    private List<Link> links=new ArrayList<>();
    public void addLink(Link link) {
        links.add(link);
    }

    public Long getReducerSummaryId() {
        return reducerSummaryId;
    }

    public void setReducerSummaryId(Long reducerSummaryId) {
        this.reducerSummaryId = reducerSummaryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<ReadReducerDto> getReducerDtos() {
        return reducerDtos;
    }

    public void setReducerDtos(List<ReadReducerDto> reducerDtos) {
        this.reducerDtos = reducerDtos;
    }

    public List<ReducerSlipDto> getReducerBetSlipDtos() {
        return reducerBetSlipDtos;
    }

    public void setReducerBetSlipDtos(List<ReducerSlipDto> reducerBetSlipDtos) {
        this.reducerBetSlipDtos = reducerBetSlipDtos;
    }

    public List<ReadMatchDto> getMatchDtos() {
        return matchDtos;
    }

    public void setMatchDtos(List<ReadMatchDto> matchDtos) {
        this.matchDtos = matchDtos;
    }

    public List<BlockDto> getBlockDtos() {
        return blockDtos;
    }

    public void setBlockDtos(List<BlockDto> blockDtos) {
        this.blockDtos = blockDtos;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void setShuffleCombinations(List<Shuffle> shuffleCombinations) {
        this.shuffleCombinations = shuffleCombinations;
    }

    public List<Shuffle> getShuffleCombinations() {
        return shuffleCombinations;
    }
}
