package project.adapter.out.persistence.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.adapter.out.persistence.Embeddables.BlockEmb;
import project.adapter.out.persistence.EntityModels.MomoEntites.MatchEventPickEmbd;
import project.adapter.out.persistence.EntityModels.ReducerBetSlipEntity;
import project.adapter.out.persistence.EntityModels.ReducerEntity;
import project.domain.model.BetSlip;
import project.domain.model.Match;
import project.domain.model.MatchOutComePick;
import project.domain.model.Money;
import project.domain.model.Reducer.Block;
import project.domain.model.Reducer.Reducer;
import project.domain.model.Reducer.ReducerBetSlip;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class ReducerMapper {
    @Inject
    BettingAccountMapper betMapper;

    public Reducer toReducerDomain(ReducerEntity entity) {
        var domain = new Reducer(new Money(entity.getTotalStake()), new Money(entity.getBonusAmount()));
        domain.setId(entity.getId());
        domain.setMatchVersion(entity.getMatchVersion());
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
        if (dom.getBlocks() != null)
            entity.setBlockEmbs(dom.getBlocks().stream().map(this::toEmbedBlock).collect(Collectors.toCollection(ArrayList::new)));
        if (dom.getSlips() != null)
            for (ReducerBetSlip e : dom.getSlips()) {
                entity.addBetSlipEntity(toBetSlipEmbed(e));
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
                entity.addBetSlipEntity(toBetSlipEmbed(e));
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
        source.updateRemainingStake(target.getRemainingStake(),target.getTotalOdds());
        target.setRemainingStake(source.getRemainingStake().getValue());
        target.setTotalOdds(source.getTotalOdds());
        target.setBrokerType(source.getBrokerType());
        target.setCategory(source.getCategory());
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
         if (refreshed.getSlips() != null) {
            for (int i = 0; i < refreshed.getSlips().size(); i++) {
                refreshSlip(entity.getSlips().get(i), refreshed.getSlips().get(i));
            }
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
                for (MatchOutComePick p : betSlip.getPicks()) {
                    out.addMatchEventPickEntity(new MatchEventPickEmbd(p.getIdentity(), p.getMatchKey(), p.getOutcomeName(), p.getOdd(), p.getLeague()));
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
                    out.addMatchEventPick(new MatchOutComePick(p.getIdentity(), p.getMatchKey(), p.getOutcomeName(), p.getOdd(), p.getLeague()));
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
        return new Block(emb.getType(), emb.getStartMatchIdx(), emb.getEndMatchIdx());
    }
}
