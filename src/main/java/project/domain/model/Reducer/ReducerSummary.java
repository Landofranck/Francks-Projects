package project.domain.model.Reducer;

import project.adapter.in.web.Utils.Code;
import project.application.error.ValidationException;
import project.domain.model.Match;
import project.domain.model.Money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReducerSummary {
    private Long reducerSummaryId;
    private String description;
    private List<Shuffle> shuffleCombinations;
    private List<Reducer> reducers;
    private Money stake;
    private Money totalStaked;
    private BigDecimal lossOrGain;
    private List<ReducerBetSlip> reducerBetSlips;
    private List<Match> matches;
    private List<Block> blocks;

    public ReducerSummary(String reducerSummaryDescription) {
        this.description= Objects.requireNonNull(reducerSummaryDescription,"you cannot create a reducerSummary without a reducerSummaryDescription reducer summary 27");
        this.matches = new ArrayList<>();
        this.reducerBetSlips = new ArrayList<>();
        this.shuffleCombinations = new ArrayList<>();
        this.reducers = new ArrayList<>();
        this.stake=new Money(0);
        this.totalStaked=new Money(0);
        this.lossOrGain=new BigDecimal(0);
        this.blocks = new ArrayList<>();
    }
    public void placeMatches() {
        matches.clear();
        if (reducers.isEmpty())
            return;
        matches.addAll(reducers.getFirst().getBetMatches());
        reducers.getFirst().checkSchuffle();
        shuffleCombinations.clear();
        shuffleCombinations=reducers.getFirst().getShuffleCombinations();
    }

    public void addReducer(Reducer reducer) {
        checMatches(reducer.getBetMatches());
        if (matches.isEmpty())
            this.matches = reducer.getBetMatches();
        this.reducers.add(reducer);
    }

    public void computeSummaryReducerSlips() {
        if (reducers.isEmpty())
            return;
        reducerBetSlips = reducers.getFirst().getSlips();
        for (int i = 1; i < reducerBetSlips.size(); i++) {
            var slip = reducers.get(i).getSlips();
            for (int j=0;j<slip.size();j++) {
                if (reducerBetSlips.get(j).getTotalOdds() > slip.get(j).getTotalOdds()) {}
            }
        }
        checkValues();
    }

    private void checMatches(List<Match> matches) {

        for (int i = 0; i < matches.size(); i++) {
            var test = (this.matches.get(i).getHome().equals(matches.get(i).getHome()))
                    && (this.matches.get(i).getAway() == matches.get(i).getAway())
                    && (this.matches.get(i).getMatchLeague() == matches.get(i).getMatchLeague());
            if (!test) {
                throw new ValidationException(Code.REDUCER_ERROR, "You can only add reducers with the same set of matches: reducer summary 37", Map.of("reducerSummaryId", reducerSummaryId));
            }
        }
    }
    private void checkValues(){
        this.totalStaked=new Money(BigDecimal.ZERO);
        for (ReducerBetSlip s : reducerBetSlips) {
            this.totalStaked=totalStaked.add(s.getPlanedStake());
        }
        lossOrGain=stake.getValue().subtract(totalStaked.getValue());
    }
    public Long getReducerSummaryId() {
        return reducerSummaryId;
    }

    public Money getStake() {
        return stake;
    }

    public void setStake(Money stake) {
        this.stake = stake;
    }

    public Money getTotalStaked() {
        return totalStaked;
    }

    public void setTotalStaked(Money totalStaked) {
        this.totalStaked = totalStaked;
    }

    public BigDecimal getLossOrGain() {
        return lossOrGain;
    }

    public void setLossOrGain(BigDecimal lossOrGain) {
        this.lossOrGain = lossOrGain;
    }

    public List<Reducer> getReducers() {
        return reducers;
    }

    public void setReducers(List<Reducer> reducers) {
        this.reducers = reducers;
    }

    public List<ReducerBetSlip> getReducerBetSlips() {
        return reducerBetSlips;
    }

    public void setReducerBetSlips(List<ReducerBetSlip> reducerBetSlips) {
        this.reducerBetSlips = reducerBetSlips;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public void setReducerSummaryId(Long reducerSummaryId) {
        this.reducerSummaryId = reducerSummaryId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public List<Shuffle> getShuffleCombinations() {
        return shuffleCombinations;
    }

    public void setShuffleCombinations(List<Shuffle> shuffleCombinations) {
        this.shuffleCombinations = shuffleCombinations;
    }
}
