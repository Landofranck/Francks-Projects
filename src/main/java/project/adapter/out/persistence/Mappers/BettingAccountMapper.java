package project.adapter.out.persistence.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import project.adapter.out.persistence.EntityModels.*;
import project.adapter.out.persistence.EntityModels.BettingAccount.*;
import project.domain.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BettingAccountMapper {
    public BettingAccountEntity toBettingAccountEntity(BettingAccount domainModel) {
        var entityModel = new BettingAccountEntity();
        entityModel.setBalance(domainModel.getBalance().getValue());
        entityModel.setBrokerType(domainModel.getBrokerType());
        entityModel.setAccountName(domainModel.getAccountName());
        if (domainModel.getDraftBetSlip() != null) {
            entityModel.putNewBetSlip(toDraftSlipEntity(domainModel.getDraftBetSlip()));//the part send the error     "message": "Cannot invoke \"project.adapter.out.persistence.EntityModels.BettingAccount.BetSlipEntity.setStatus(project.domain.model.Enums.BetStatus)\" because the return value of \"project.adapter.out.persistence.EntityModels.BettingAccount.BettingAccountEntity.$$_hibernate_read_draftBetSlip()\" is null"
        }

        if (domainModel.getTransactionHistory() != null) {
            for (Transaction T : domainModel.getTransactionHistory()) {
                entityModel.addTransactionEntity(toBettingAccountTransactionEntity(T));
            }
        }
        if (domainModel.getBetHistory() != null) {
            for (BetSlip b : domainModel.getBetHistory()) {
                entityModel.addBetSlipEntity(toBetslipEntity(b));
            }
        }
        return entityModel;
    }

    public BettingAccount toBettingAccountDomain(BettingAccountEntity entityModel) {
        var domainModel = new BettingAccount(entityModel.getAccountName(), entityModel.getBrokerType());
        domainModel.setBalance(new Money(entityModel.getBalance()));
        domainModel.setId(entityModel.getId());
        if (entityModel.getDraftBetSlip() != null) {
            domainModel.putEmptySlip(toDraftSlipDomain(entityModel.getDraftBetSlip()));
        }
        if (entityModel.getTransactionHistory() != null) {
            for (BettingAccountTransactionEntity T : entityModel.getTransactionHistory()) {
                domainModel.addTransaction(toBettingTransactionDomain(T));
            }
        }
        if (entityModel.getBetHistory() != null) {
            for (BetSlipEntity b : entityModel.getBetHistory()) {
                domainModel.addBetSlip(toBetslipDomain(b));
            }
        }
        return domainModel;
    }

    public BettingAccountTransactionEntity toBettingAccountTransactionEntity(Transaction domainTransaction) {
        var transactionEntity = new BettingAccountTransactionEntity();
        transactionEntity.setAccountBalanceAfterTransaction(domainTransaction.getAccountBalanceAfterTransaction().getValue());
        transactionEntity.setTransactionAmmount(domainTransaction.getTransactionAmmount().getValue());
        transactionEntity.setCreatedAt(domainTransaction.getCreatedAt());
        transactionEntity.setType(domainTransaction.getType());
        transactionEntity.setDescription(domainTransaction.getDescription());
        //set owner not created because this is done in parent class already with setParent(this)
        return transactionEntity;
    }

    public MatchEntity toMatchEntity(Match domainPick) {
        var matchEntity = new MatchEntity();
        matchEntity.setAway(domainPick.getAway());
        matchEntity.setHome(domainPick.getHome());
        if (domainPick.getMatchOutComes() == null || domainPick.getMatchOutComes().isEmpty())
            throw new RuntimeException("matchmust have outcomes line 78 mapper");
        for (MatchEventPick m : domainPick.getMatchOutComes()) {
            matchEntity.addOutcome(toMatchOutcomeEntity(m));
        }
        return matchEntity;
    }

    public MatchEventPickEntity toMatchEventEntity(MatchEventPick domainPick) {
        var matchEventPickEntity = new MatchEventPickEntity();
        matchEventPickEntity.setMatchKey(domainPick.getMatchKey());
        matchEventPickEntity.setOdd(domainPick.getOdd());
        matchEventPickEntity.setOutcomeName(domainPick.getOutcomeName());
        return matchEventPickEntity;
    }


    private MatchOutcomeEntity toMatchOutcomeEntity(MatchEventPick m) {
        var outcomeEntity = new MatchOutcomeEntity();
        outcomeEntity.setOutcomeName(m.getOutcomeName());
        outcomeEntity.setOdd(m.getOdd());
        outcomeEntity.setMatchKey(m.getMatchKey());
        return outcomeEntity;
    }




    public Transaction toBettingTransactionDomain(BettingAccountTransactionEntity e) {
        var transacttionDomain = new Transaction(new Money(e.getTransactionAmmount()), new Money(e.getAccountBalanceAfterTransaction()), e.getCreatedAt(), e.getType(), e.getDescription());
        //set owner not created because this is done in parent class already with setParent(this)
        transacttionDomain.setId(e.getId());
        return transacttionDomain;
    }



    public DraftBetSlip toDraftSlipDomain(DraftSlipEntity draftSlipEntity) {
        var draftDomain = new DraftBetSlip(draftSlipEntity.getCategory());
        draftDomain.setId(draftSlipEntity.getId());
        draftDomain.setCreatedAt(draftSlipEntity.getCreatedAt());
        draftDomain.setStatus(draftSlipEntity.getStatus());
        draftDomain.setTotalOdds(draftSlipEntity.getTotalOdd());
        if (draftSlipEntity.getStake() != null) {
            draftDomain.setStake(new Money(draftSlipEntity.getStake()));
        }
        if (draftSlipEntity.getPicks() != null) {
            for (DraftEventPickEntity p : draftSlipEntity.getPicks()) {
                draftDomain.addMatchEventPick(toDraftEventDomain(p));
            }
        }
        var draftslipOwner=new BettingAccount(draftSlipEntity.getDraftBetSlipOwner().getAccountName(),
                draftSlipEntity.getDraftBetSlipOwner().getBrokerType());
        draftslipOwner.setId(draftSlipEntity.getDraftBetSlipOwner().getId());
        draftDomain.setDraftSlipOwner(draftslipOwner);
        return draftDomain;
    }
    public BetSlip toBetslipDomain(BetSlipEntity betSlipEntity) {
        var betSlipDomain = new BetSlip(betSlipEntity.getCategory());
        betSlipDomain.setId(betSlipEntity.getId());
        betSlipDomain.setPotentialWinning(new Money(betSlipEntity.getPotentialWinning()));
        betSlipDomain.setCreatedAt(betSlipEntity.getCreatedAt());
        betSlipDomain.setStatus(betSlipEntity.getStatus());
        betSlipDomain.setTotalOdds(betSlipEntity.getTotalOdd());
        if (betSlipEntity.getStake() != null) {
            betSlipDomain.setStake(new Money(betSlipEntity.getStake()));
        }
        if (betSlipEntity.getPicks() != null) {
            for (MatchEventPickEntity p : betSlipEntity.getPicks()) {
                betSlipDomain.addMatchEventPick(toMatchEventDomain(p));
            }
        }
        return betSlipDomain;
    }

    public BetSlipEntity toBetslipEntity(BetSlip betSlip) {
        try {


            var betSlipentity = new BetSlipEntity();
            betSlipentity.setPotentialWinning(betSlip.getPotentialWinning().getValue());
            betSlipentity.setCategory(betSlip.getCategory());
            betSlipentity.setStatus(betSlip.getStatus());
            betSlipentity.setCreatedAt(betSlip.getCreatedAt());
            betSlipentity.setStake(betSlip.getStake().getValue());
            if (betSlip.getPicks() != null) {
                for (MatchEventPick p : betSlip.getPicks()) {
                    betSlipentity.addMatchEventPickEntity(toMatchEventEntity(p));
                }
            }
            return betSlipentity;
        } catch (Exception e) {
            throw new RuntimeException("could not convert to betslip entity jpa line 164" + e.getMessage());
        }
    }

    public DraftSlipEntity toDraftSlipEntity(DraftBetSlip betSlip) {
        try {


            var draftSlipEntity = new DraftSlipEntity();
            draftSlipEntity.setPotentialWinning(betSlip.getPotentialWinning().getValue());
            draftSlipEntity.setCategory(betSlip.getCategory());
            draftSlipEntity.setStatus(betSlip.getStatus());
            draftSlipEntity.setCreatedAt(betSlip.getCreatedAt());
            draftSlipEntity.setStake(betSlip.getStake().getValue());
            if (betSlip.getPicks() != null) {
                for (MatchEventPick p : betSlip.getPicks()) {
                    draftSlipEntity.addDraftEventPick(toDraftEventEntity(p));
                }
            }
            return draftSlipEntity;
        } catch (Exception e) {
            throw new RuntimeException("could not convert to betslip entity jpa line 164" + e.getMessage());
        }
    }
    public DraftEventPickEntity toDraftEventEntity(MatchEventPick domainPick) {
        var draftEventPickEntity = new DraftEventPickEntity();
        draftEventPickEntity.setMatchKey(domainPick.getMatchKey());
        draftEventPickEntity.setOdd(domainPick.getOdd());
        draftEventPickEntity.setOutcomeName(domainPick.getOutcomeName());
        return draftEventPickEntity;
    }
    public MatchEventPick toDraftEventDomain(DraftEventPickEntity p) {
        var draftEvent = new MatchEventPick(p.getMatchKey(), p.outcomeName(), p.getOdd());
        draftEvent.setMatchKey(p.getMatchKey());
        return draftEvent;
    }

    public MatchEventPick toMatchEventDomain(MatchEventPickEntity p) {
        var matchEventPickDomain = new MatchEventPick(p.getMatchKey(), p.outcomeName(), p.getOdd());
        matchEventPickDomain.setMatchKey(p.getMatchKey());
        return matchEventPickDomain;
    }

    public MatchEventPick toMatchOutcomeDomain(MatchOutcomeEntity p) {
        var matchEventPickDomain = new MatchEventPick(p.getMatchKey(), p.getOutcomeName(), p.getOdd());
        matchEventPickDomain.setMatchKey(p.getOutcomeName());
        return matchEventPickDomain;
    }



    public Match toMatchDomain(MatchEntity eM) {
        var dom = new Match(eM.getHome(), eM.getAway());
        dom.setMatchId(eM.getId());
        dom.setAway(eM.getAway());
        dom.setHome(eM.getHome());
        if (eM.getOutcomes().isEmpty()) dom.setMatchOutComes(new ArrayList<>());
        {
        }
        if (eM.getOutcomes() != null) {
            for (MatchOutcomeEntity e : eM.getOutcomes()) {
                dom.addPick(toMatchOutcomeDomain(e));
            }
        }
        return dom;
    }

    public List<BettingAccount> toListOfAccountDomains(List<BettingAccountEntity> list) {
        return list.stream().map(this::toBettingAccountDomain).collect(Collectors.toCollection(ArrayList::new));
    }



    public List<Match> toMatchDomains(List<MatchEntity> matchEntities) {
        return matchEntities.stream().map(this::toMatchDomain).collect(Collectors.toCollection(ArrayList::new));
    }

    public BetSlipEntity fromDraftToBetslipEntity(DraftBetSlip draftSlip) {
        var betSlipentity = new BetSlipEntity();
        betSlipentity.setStatus(draftSlip.getStatus());
        betSlipentity.setCreatedAt(draftSlip.getCreatedAt());
        betSlipentity.setStake(draftSlip.getStake().getValue());
        betSlipentity.setPotentialWinning(draftSlip.getPotentialWinning().getValue());
        betSlipentity.setCategory(draftSlip.getCategory());
        betSlipentity.setTotalOdd(draftSlip.getTotalOdds());
        if (draftSlip.getPicks() != null) {
            for (MatchEventPick p : draftSlip.getPicks()) {
                betSlipentity.addMatchEventPickEntity(toMatchEventEntity(p));
            }
        }
        return betSlipentity;
    }
}
