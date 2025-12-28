package project.adapter.out.persistence.EntityModels;

import jakarta.enterprise.context.ApplicationScoped;
import project.domain.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class Mapper {
    public BettingAccountEntity toBettingAccountEntity(BettingAccount domainModel) {
        var entityModel = new BettingAccountEntity();
        entityModel.setBalance(domainModel.getBalance().getValue());
        entityModel.setBrokerType(domainModel.getBrokerType());
        entityModel.setAccountName(domainModel.getAccountName());
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

    public BettingAccountTransactionEntity toBettingAccountTransactionEntity(Transaction domainTransaction) {
        var transactionEntity = new BettingAccountTransactionEntity();
        transactionEntity.setAccountBalanceAfterTransaction(domainTransaction.getAccountBalanceAfterTransaction().getValue());
        transactionEntity.setTransactionAmmount(domainTransaction.getTransactionAmmount().getValue());
        transactionEntity.setCreatedAt(domainTransaction.getCreatedAt());
        transactionEntity.setType(domainTransaction.getType());
        //set owner not created because this is done in parent class already with setParent(this)
        return transactionEntity;
    }
    public MomoAccountTransactionEntity toMomoTransactionEntity(Transaction domainTransaction) {
        var transactionEntity = new MomoAccountTransactionEntity();
        transactionEntity.setAccountBalanceAfterTransaction(domainTransaction.getAccountBalanceAfterTransaction().getValue());
        transactionEntity.setTransactionAmmount(domainTransaction.getTransactionAmmount().getValue());
        transactionEntity.setCreatedAt(domainTransaction.getCreatedAt());
        transactionEntity.setType(domainTransaction.getType());
        //set owner not created because this is done in parent class already with setParent(this)
        return transactionEntity;
    }

    public BetSlipEntity toBetslipEntity(BetSlip betSlip) {
        var betSlipentity = new BetSlipEntity();
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
    }

    public MatchEventPickEntity toMatchEventEntity(MatchEventPick domainPick) {
        var matchEventPickEntity = new MatchEventPickEntity();
        matchEventPickEntity.setMatchKey(domainPick.getMatchKey());
        matchEventPickEntity.setOdd(domainPick.getOdd());
        matchEventPickEntity.setOutcomeName(domainPick.getOutcomeName());
        return matchEventPickEntity;
    }

    public MobileMoneyAccountsEntity toMobileMoneyEntity(MobileMoneyAccount momo) {
        var entitMomo = new MobileMoneyAccountsEntity();
        entitMomo.setId(momo.getAccountId());
        entitMomo.setAccountBalance(momo.getAccountBalance().getValue());
        entitMomo.setAccountType(momo.getAccountType());
        entitMomo.setDailyLimit(momo.getDailyLimit());
        entitMomo.setMonthlyLimit(momo.getMonthlyLimit());
        entitMomo.setWeeklyLimit(momo.getWeeklyLimit());
        if (momo.getTransactionHistory() != null) {
            for (Transaction t : momo.getTransactionHistory()) {
                entitMomo.addBettingaccoutTransactionEntity(toMomoTransactionEntity(t));
            }
        }
        return entitMomo;
    }


    public BettingAccount toBettingAccountDomain(BettingAccountEntity entityModel) {
        var domainModel = new BettingAccount(entityModel.getAccountName(),entityModel.getBrokerType());
        domainModel.setBalance(new Money(entityModel.getBalance()));
        domainModel.setId(entityModel.getId());
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

    public Transaction toBettingTransactionDomain(BettingAccountTransactionEntity e) {
        var transacttionDomain = new Transaction(new Money(e.getTransactionAmmount()),new Money(e.getAccountBalanceAfterTransaction()),e.getCreatedAt(),e.getType());
        //set owner not created because this is done in parent class already with setParent(this)
        transacttionDomain.setId(e.getId());
        return transacttionDomain;
    }
    public Transaction toMomoTransactionDomain(MomoAccountTransactionEntity e) {
        var transacttionDomain = new Transaction(new Money(e.getTransactionAmmount()),new Money(e.getAccountBalanceAfterTransaction()),e.getCreatedAt(),e.getType());
        //set owner not created because this is done in parent class already with setParent(this)
        transacttionDomain.setId(e.getId());
        return transacttionDomain;
    }

    public BetSlip toBetslipDomain(BetSlipEntity betSlipEntity) {
        var betSlipDomain = new BetSlip(betSlipEntity.getCategory());
        betSlipDomain.setCreatedAt(betSlipEntity.getCreatedAt());
        betSlipDomain.setStake(new Money(betSlipEntity.getStake()));
        betSlipDomain.setStatus(betSlipEntity.getStatus());
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

    public MatchEventPick toMatchEventDomain(MatchEventPickEntity p) {
        var matchEventPickDomain = new MatchEventPick(p.outcomeName(), p.getOdd());
        matchEventPickDomain.setMatchKey(p.getMatchKey());
        return matchEventPickDomain;
    }

    public MobileMoneyAccount toMobileMoneyDomain(MobileMoneyAccountsEntity m) {
        var domainMomo = new MobileMoneyAccount(m.getId(), m.getAccountType());
        domainMomo.setId(m.getId());
        domainMomo.setAccountBalance(new Money(m.getAccountBalance()));
        domainMomo.setDailyLimit(m.getDailyLimit());
        domainMomo.setMonthlyLimit(m.getMonthlyLimit());
        domainMomo.setWeeklyLimit(m.getWeeklyLimit());
        if (m.getTransactionHistory() != null) {
            for (MomoAccountTransactionEntity t : m.getTransactionHistory()) {
                domainMomo.addTransaction(toMomoTransactionDomain(t));
            }
        }
        return domainMomo;
    }
    public List<BettingAccount> toListOfAccountDomains(List<BettingAccountEntity> list){
        return list.stream().map(this::toBettingAccountDomain).collect(Collectors.toCollection(ArrayList::new));
    }
    public List<MobileMoneyAccount> toListOfMOMOtDomains(List<MobileMoneyAccountsEntity> list){
        return list.stream().map(this::toMobileMoneyDomain).collect(Collectors.toCollection(ArrayList::new));
    }


}
