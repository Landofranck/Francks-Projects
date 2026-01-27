package project.adapter.out.persistence.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.adapter.out.persistence.EntityModels.ReducerEntity;
import project.domain.model.BetSlip;
import project.domain.model.Match;
import project.domain.model.Money;
import project.domain.model.Reducer;

import java.util.ArrayList;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReducerMapper {
    @Inject
    BettingAccountMapper betMapper;
    public Reducer toReducerDomain(ReducerEntity entity) {
        var domain = new Reducer(new Money(entity.getTotalStake()), new Money(entity.getBonusAmount()));
        domain.setId(entity.getId());
        if(entity.getSlips()!=null)
            domain.setSlips(entity.getSlips().stream().map(betMapper::toBetslipDomain).collect(Collectors.toCollection(ArrayList::new)));
        if(entity.getSlips()!=null)
            domain.setBetMatches(entity.getBetMatcheEntities().stream().map(betMapper::toMatchDomain).collect(Collectors.toCollection(ArrayList::new)));
        domain.setTheSlipStakes();
        return domain;
    }
    public ReducerEntity toReducerEntity(Reducer dom) {
        var entity = new ReducerEntity();
        entity.setBonusAmount(dom.getBonusAmount().getValue());
        entity.setTotalStake(dom.getTotalStake().getValue());
        if(dom.getSlips()!=null)
            for (BetSlip e: dom.getSlips()){
                entity.addBetSlipEntity(betMapper.toBetslipEntity(e));
            }
        if(dom.getBetMatches()!=null)
            for(Match m: dom.getBetMatches()){
                entity.addMatches(betMapper.toMatchEntity(m));
            }
        return entity;
    }
    public void applyChangesToReducer(ReducerEntity entity, Reducer dom){

        entity.setBonusAmount(dom.getBonusAmount().getValue());
        entity.setTotalStake(dom.getTotalStake().getValue());
        if(dom.getSlips()!=null)
            for (BetSlip e: dom.getSlips()) {
                entity.addBetSlipEntity(betMapper.toBetslipEntity(e));
            }
    }
}
