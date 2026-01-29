package project.adapter.out.persistence.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.adapter.out.persistence.Embeddables.BlockEmb;
import project.adapter.out.persistence.EntityModels.MomoEntites.MatchEventPickEmbd;
import project.adapter.out.persistence.EntityModels.ReducerBetSlipEntity;
import project.adapter.out.persistence.EntityModels.ReducerEntity;
import project.domain.model.Match;
import project.domain.model.MatchEventPick;
import project.domain.model.Money;
import project.domain.model.Reducer.Block;
import project.domain.model.Reducer.Reducer;
import project.domain.model.Reducer.ReducerBetSlip;

import java.util.ArrayList;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReducerMapper {
    @Inject
    BettingAccountMapper betMapper;

    public Reducer toReducerDomain(ReducerEntity entity) {
        var domain = new Reducer(new Money(entity.getTotalStake()), new Money(entity.getBonusAmount()));
        domain.setId(entity.getId());
        if (entity.getBlockEmbs() != null)
            domain.setBlocks(entity.getBlockEmbs().stream().map(this::toBlockDomain).collect(Collectors.toCollection(ArrayList::new)));

        if (entity.getSlips() != null)
            domain.setSlips(entity.getSlips().stream().map(this::toReducerBetSlipDomain).collect(Collectors.toCollection(ArrayList::new)));
        if (entity.getBetMatcheEntities() != null)
            domain.setBetMatches(entity.getBetMatcheEntities().stream().map(betMapper::toMatchDomain).collect(Collectors.toCollection(ArrayList::new)));
        domain.setTheSlipStakes();
        return domain;
    }

    public ReducerEntity toReducerEntity(Reducer dom) {
        var entity = new ReducerEntity();
        entity.setBonusAmount(dom.getBonusAmount().getValue());
        entity.setTotalStake(dom.getTotalStake().getValue());
        if (dom.getBlocks() != null)
            entity.setBlockEmbs(dom.getBlocks().stream().map(this::toEmbedBlock).collect(Collectors.toCollection(ArrayList::new)));
        if (dom.getSlips() != null)
            for (ReducerBetSlip e : dom.getSlips()) {
                entity.addBetSlipEmbd(toBetSlipEmbed(e));
            }
        if (dom.getBetMatches() != null)
            for (Match m : dom.getBetMatches()) {
                entity.addMatches(betMapper.toMatchEntity(m));
            }
        return entity;
    }

    public void applyChangesToReducer(ReducerEntity entity, Reducer dom) {

        entity.setBonusAmount(dom.getBonusAmount().getValue());
        entity.setTotalStake(dom.getTotalStake().getValue());
        if (dom.getSlips() != null) {
            entity.getSlips().clear();
            for (ReducerBetSlip e : dom.getSlips()) {
                entity.addBetSlipEmbd(toBetSlipEmbed(e));
            }
        }
        if (dom.getBlocks() != null) {
            entity.getBlockEmbs().clear();
            entity.getBlockEmbs().addAll(dom.getBlocks().stream()
                    .map(this::toEmbedBlock).collect(Collectors.toCollection(ArrayList::new)));
        }
    }

    public ReducerBetSlipEntity toBetSlipEmbed(ReducerBetSlip betSlip) {
        try {
            var out = new ReducerBetSlipEntity();
            out.setPotentialWinning(betSlip.getPotentialWinning().getValue());
            out.setCategory(betSlip.getCategory());
            out.setBrokerType(betSlip.getBrokerType());
            out.setPlanedStake(betSlip.getPlanedStake().getValue());
            out.setTotalOdds(betSlip.getTotalOdds());
            out.setRemainingStake(betSlip.getRemainingStake().getValue());
            out.setNumberOfEvents(betSlip.getNumberOfEvents());
            if (betSlip.getPicks() != null) {
                for (MatchEventPick p : betSlip.getPicks()) {
                    out.addMatchEventPickEntity(new MatchEventPickEmbd(p.getIdentity(), p.getMatchKey(), p.getOutcomeName(), p.getOdd()));
                }
            }
            return out;
        } catch (Exception e) {
            throw new RuntimeException("could not convert to betslip entity jpa line 164" + e.getMessage());
        }
    }

    public ReducerBetSlip toReducerBetSlipDomain(ReducerBetSlipEntity betSlip) {
        try {
            var out = new ReducerBetSlip(betSlip.getCategory());
            if (betSlip.getPicks() != null) {
                for (MatchEventPickEmbd p : betSlip.getPicks()) {
                    out.addMatchEventPick(new MatchEventPick(p.getIdentity(), p.getMatchKey(), p.getOutcomeName(), p.getOdd()));
                }
            }

            out.setPotentialWinning(new Money(betSlip.getPotentialWinning()));
            out.setCategory(betSlip.getCategory());
            out.setBrokerType(betSlip.getBrokerType());
            out.setPlanedStake(new Money(betSlip.getPlanedStake()));
            out.setTotalOdds(betSlip.getTotalOdds());
            out.setNumberOfEvents(betSlip.getNumberOfEvents());
            out.setRemainingStake(new Money(betSlip.getRemainingStake()));
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
        var out = new Block(emb.getType(), emb.getStartMatchIdx(), emb.getEndMatchIdx());
        return out;
    }
}
