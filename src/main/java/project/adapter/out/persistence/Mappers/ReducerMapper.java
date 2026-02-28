package project.adapter.out.persistence.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.adapter.out.persistence.Embeddables.BlockEmb;
import project.adapter.out.persistence.EntityModels.MomoEntites.MatchEventPickEmbd;
import project.adapter.out.persistence.EntityModels.ReducerBetSlipEntity;
import project.adapter.out.persistence.EntityModels.ReducerEntity;
import project.adapter.out.persistence.EntityModels.ReducerSummaryEntity;
import project.adapter.out.persistence.EntityModels.ShuffleEmb;
import project.domain.model.Match;
import project.domain.model.MatchOutComePick;
import project.domain.model.Money;
import project.domain.model.Reducer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReducerMapper {
    @Inject
    BettingAccountMapper betMapper;

    public Reducer toReducerDomain(ReducerEntity entity) {
        var domain = new Reducer(new Money(entity.getTotalStake()), new Money(entity.getBonusAmount()), entity.getStrategy(), entity.getBroker());
        domain.setId(entity.getId());
        domain.setMatchVersion(entity.getMatchVersion());
        domain.setStrategy(entity.getStrategy());
        domain.setBroker(entity.getBroker());
        domain.setProfitOrLoss(entity.getProfitOrLoss());
        domain.setTotalStaked(new Money(entity.getTotalStaked()));
        domain.setShuffleCombinations(entity.getShuffleCombinations().stream().map(this::toShuffleDomain).toList());
        if (entity.getBlockEmbs() != null)
            domain.setBlocks(entity.getBlockEmbs().stream().map(this::toBlockDomain).collect(Collectors.toCollection(ArrayList::new)));

        if (entity.getSlips() != null)
            domain.setSlips(entity.getSlips().stream().map(this::toReducerBetSlipDomain).collect(Collectors.toCollection(ArrayList::new)));
        if (entity.getBetMatcheEntities() != null)
            domain.setBetMatches(entity.getBetMatcheEntities().stream().map(betMapper::toMatchDomain).collect(Collectors.toCollection(ArrayList::new)));

        return domain;
    }

    public ReducerEntity toReducerEntity(Reducer dom) {
        var entity = new ReducerEntity();
        entity.setBonusAmount(dom.getBonusAmount().getValue());
        entity.setTotalStake(dom.getTotalStake().getValue());
        entity.setMatchVersion(entity.getMatchVersion());
        entity.setStrategy(dom.getStrategy());
        entity.setBroker(dom.getBroker());
        entity.setProfitOrLoss(dom.getProfitOrLoss());
        entity.setTotalStaked(dom.getTotalStaked().getValue());
        entity.setShuffleCombinations(dom.getShuffleCombinations().stream().map(this::toshuffleEntity).collect(Collectors.toCollection(ArrayList::new)));
        if (dom.getBlocks() != null)
            entity.setBlockEmbs(dom.getBlocks().stream().map(this::toEmbedBlock).collect(Collectors.toCollection(ArrayList::new)));
        if (dom.getSlips() != null)
            for (ReducerBetSlip e : dom.getSlips()) {
                entity.addBetSlipEntity(toBetSlipEntity(e));
            }
        if (dom.getBetMatches() != null)
            for (Match m : dom.getBetMatches()) {
                entity.addMatches(betMapper.toMatchEntity(m));
            }
        return entity;
    }

    private ShuffleEmb toshuffleEntity(Shuffle shuf) {
        return new ShuffleEmb(shuf.matchIds());
    }

    private Shuffle toShuffleDomain(ShuffleEmb longs) {
        return new Shuffle(longs.getShuffle());
    }

    public void applyChangesToReducer(ReducerEntity entity, Reducer dom) {

        entity.setBonusAmount(dom.getBonusAmount().getValue());
        entity.setTotalStake(dom.getTotalStake().getValue());
        entity.setShuffleCombinations(dom.getShuffleCombinations().stream().map(this::toshuffleEntity).toList());
        entity.setStrategy(dom.getStrategy());
        entity.setBroker(dom.getBroker());
        entity.setProfitOrLoss(dom.getProfitOrLoss());
        entity.setTotalStaked(dom.getTotalStaked().getValue());
        if (dom.getSlips() != null) {
            entity.getSlips().clear();
            for (ReducerBetSlip e : dom.getSlips()) {
                entity.addBetSlipEntity(toBetSlipEntity(e));
            }
        }

        if (dom.getBlocks() != null) {
            entity.getBlockEmbs().clear();
            entity.getBlockEmbs().addAll(dom.getBlocks().stream()
                    .map(this::toEmbedBlock).collect(Collectors.toCollection(ArrayList::new)));
        }
    }

    //this method changes the value of the old betslip, while making sure if the new slip has the proper amount for remaining stake bases on how the slips odds change
    public void refreshSlip(ReducerBetSlipEntity target, ReducerBetSlip source) {
        source.updateRemainingStake(target.getRemainingStake(), target.getTotalOdds());
        target.setRemainingStake(source.getRemainingStake().getValue());
        target.setTotalOdds(source.getTotalOdds());
        target.setBrokerType(source.getBrokerType());
        target.setCategory(source.getCategory());
        target.setBetStrategy(source.getBetStrategy());
        if (source.getPicks() != null) {
            target.getPicks().clear();
            for (MatchOutComePick p : source.getPicks()) {
                target.addMatchEventPickEntity(new MatchEventPickEmbd(p.getIdentity(), p.getMatchKey(), p.getOutcomeName(), p.getOdd(), p.getLeague()));
            }
        }
    }

    public void refreshChangesReducer(ReducerEntity entity, Reducer refreshed) {

        entity.setBonusAmount(refreshed.getBonusAmount().getValue());
        entity.setTotalStake(refreshed.getTotalStake().getValue());
        entity.setMatchVersion(refreshed.getMatchVersion());
        entity.setStrategy(refreshed.getStrategy());
        if (refreshed.getSlips() != null) {
            for (int i = 0; i < refreshed.getSlips().size(); i++) {
                refreshSlip(entity.getSlips().get(i), refreshed.getSlips().get(i));
            }
        }

    }

    public ReducerBetSlipEntity toBetSlipEntity(ReducerBetSlip betSlip) {
        try {
            var out = new ReducerBetSlipEntity();
            out.setPotentialWinning(betSlip.getPotentialWinning().getValue());
            out.setCategory(betSlip.getCategory());
            out.setBrokerType(betSlip.getBrokerType());
            out.setPlanedStake(betSlip.getPlanedStake().getValue());
            out.setTotalOdds(betSlip.getTotalOdds());
            out.setRemainingStake(betSlip.getRemainingStake().getValue());
            out.setNumberOfEvents(betSlip.getNumberOfEvents());
            out.setBetStrategy(betSlip.getBetStrategy());
            out.setBonusOdds(betSlip.getBonusOdds());
            if (betSlip.getPicks() != null) {
                for (MatchOutComePick p : betSlip.getPicks()) {
                    var in = new MatchEventPickEmbd(p.getIdentity(), p.getMatchKey(), p.getOutcomeName(), p.getOdd(), p.getLeague());
                    in.setBegins(p.getBegins());
                    in.setOwnerMatchName(p.getOwnerMatchName());
                    in.setOutComePickStatus(p.getOutcomePickStatus());
                    out.addMatchEventPickEntity(in);
                }
            }
            return out;
        } catch (Exception e) {
            throw new RuntimeException("could not convert to betslip entity jpa line 164" + e.getMessage());
        }
    }

    public ReducerBetSlip toReducerBetSlipDomain(ReducerBetSlipEntity betSlip) {
        try {
            var out = new ReducerBetSlip(betSlip.getCategory(), betSlip.getBetStrategy(), betSlip.getBrokerType());
            if (betSlip.getPicks() != null) {
                for (MatchEventPickEmbd p : betSlip.getPicks()) {
                    var in = new MatchOutComePick(p.getIdentity(), p.getMatchKey(), p.getOutcomeName(), p.getOdd(), p.getLeague());
                    in.setOwnerMatchName(p.getOwnerMatchName());
                    in.setBegins(p.getBegins());
                    in.setOutcomePickStatus(p.getOutcomePickStatus());
                    out.addMatchEventPick(in);
                }
            }

            out.setPotentialWinning(new Money(betSlip.getPotentialWinning()));
            out.setCategory(betSlip.getCategory());
            out.setBrokerType(betSlip.getBrokerType());
            out.setPlanedStake(new Money(betSlip.getPlanedStake()));
            out.setTotalOdds(betSlip.getTotalOdds());
            out.setNumberOfEvents(betSlip.getNumberOfEvents());
            out.setRemainingStake(new Money(betSlip.getRemainingStake()));
            out.setBetStrategy(betSlip.getBetStrategy());
            out.setBonusOdds(betSlip.getBonusOdds());
            return out;
        } catch (Exception e) {
            throw new RuntimeException("could not convert to betslip entity mapper line 164" + e.getMessage());
        }
    }

    public BlockEmb toEmbedBlock(Block block) {
        var out = new BlockEmb();
        out.setStartMatchIdx(block.getStartMatchIdx());
        out.setType(block.getType());
        out.setEndMatchIdx(block.getEndMatchIdx());
        return out;
    }

    public Block toBlockDomain(BlockEmb emb) {
        return new Block(emb.getType(), emb.getStartMatchIdx(), emb.getEndMatchIdx());
    }

    public List<Reducer> toReducerDomains(List<ReducerEntity> reducers) {
        return reducers.stream().map(this::toReducerDomain).toList();
    }

    public ReducerSummaryEntity toReducerSummaryEntity(ReducerSummary summary) {
        var out = new ReducerSummaryEntity();
        out.setDescription(summary.getDescription());
        out.setStake(summary.getStake().getValue());
        out.setLossOrGain(summary.getLossOrGain());
        out.setTotalStaked(summary.getTotalStaked().getValue());
        if (summary.getReducers() != null)
            for (Reducer r : summary.getReducers())
                out.addReducerEntity(this.toReducerEntity(r));
        if (summary.getBlocks() != null)
            out.setBlocks(summary.getBlocks().stream().map(this::toEmbedBlock).toList());
        return out;
    }

    public ReducerSummary toReducerSummaryDomain(ReducerSummaryEntity reducerSummary) {

        var out = new ReducerSummary(reducerSummary.getDescription());
        out.setReducerSummaryId(reducerSummary.getReducerSummaryId());
        out.setStake(new Money(reducerSummary.getStake()));
        out.setLossOrGain(reducerSummary.getLossOrGain());
        out.setTotalStaked(new Money(reducerSummary.getTotalStaked()));
        if (reducerSummary.getBlocks() != null)
            out.setBlocks(reducerSummary.getBlocks().stream().map(this::toBlockDomain).collect(Collectors.toCollection(ArrayList::new)));
        if (reducerSummary.getReducerEntities() != null)
            out.setReducers(reducerSummary.getReducerEntities().stream().map(this::toReducerDomain).collect(Collectors.toCollection(ArrayList::new)));
        return out;
    }

    public List<ReducerSummary> toReducerSummaryEntityList(List<ReducerSummaryEntity> out) {
        return out.stream().map(this::toReducerSummaryDomain).toList();
    }
}
