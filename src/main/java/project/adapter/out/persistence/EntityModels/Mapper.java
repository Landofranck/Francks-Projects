package project.adapter.out.persistence.EntityModels;

import project.domain.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    public BettingAccountEntity toBettingAccountEntity(BettingAccount domainModel) {
        var entityModel = new BettingAccountEntity();
        entityModel.setBalance(domainModel.getBalance());
        entityModel.setBrokerType(domainModel.getBrokerType());
        entityModel.setAccountName(domainModel.getAccountName());
        if (domainModel.getTransactionHistory() != null) {
            for (Transaction T : domainModel.getTransactionHistory()) {
                entityModel.addTransactionEntity(toTransactionEntity(T));
            }
        }
        if (domainModel.getBetHistory() != null) {
            for (BetSlip b : domainModel.getBetHistory()) {
                entityModel.addBetSlipEntity(toBetslipEntity(b));
            }
        }
        return entityModel;
    }

    private TransactionEntity toTransactionEntity(Transaction domainTransaction) {
        var transactionEntity = new TransactionEntity();
        transactionEntity.setAccountBalanceAfterTransaction(domainTransaction.getAccountBalanceAfterTransaction());
        transactionEntity.setTransactionAmmount(domainTransaction.getTransactionAmmount());
        transactionEntity.setCreatedAt(domainTransaction.getCreatedAt());
        //set owner not created because this is done in parent class already with setParent(this)
        return transactionEntity;
    }

    public BetSlipEntity toBetslipEntity(BetSlip betSlip) {
        var betSlipentity = new BetSlipEntity();
        betSlipentity.setCategory(betSlip.getCategory());
        betSlipentity.setStatus(betSlipentity.getStatus());
        betSlipentity.setCreatedAt(betSlip.getCreatedAt());
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
        entitMomo.setAccountBalance(momo.getAccountBalance());
        entitMomo.setAccountType(momo.getAccountType());
        entitMomo.setDailyLimit(momo.getDailyLimit());
        entitMomo.setMonthlyLimit(momo.getMonthlyLimit());
        entitMomo.setWeeklyLimit(momo.getWeeklyLimit());
        if (momo.getTransactionHistory() != null) {
            for (Transaction t : momo.getTransactionHistory()) {
                entitMomo.addTransactionEntity(toTransactionEntity(t));
            }
        }
        return entitMomo;
    }


    public BettingAccount toBettingAccountDomain(BettingAccountEntity entityModel) {
        var domainModel = new BettingAccount(entityModel.getAccountName(),entityModel.getBrokerType());
        domainModel.setBalance(entityModel.getBalance());
        if (entityModel.getTransactionHistory() != null) {
            for (TransactionEntity T : entityModel.getTransactionHistory()) {
                domainModel.addTransaction(toTransactionDomain(T));
            }
        }
        if (domainModel.getBetHistory() != null) {
            for (BetSlipEntity b : entityModel.getBetHistory()) {
                domainModel.addBetSlip(toBetslipDomain(b));
            }
        }
        return domainModel;
    }

    private Transaction toTransactionDomain(TransactionEntity e) {
        var transacttionDomain = new Transaction(e.getTransactionAmmount(),e.getAccountBalanceAfterTransaction(),e.getCreatedAt(),e.getType());
        //set owner not created because this is done in parent class already with setParent(this)
        transacttionDomain.setId(e.getId());
        return transacttionDomain;
    }

    public BetSlip toBetslipDomain(BetSlipEntity betSlipEntity) {
        var betSlipDomain = new BetSlip(betSlipEntity.getCategory(),betSlipEntity.getStake(),betSlipEntity.getCreatedAt());
        betSlipDomain.setStatus(betSlipEntity.getStatus());
        betSlipDomain.setId(betSlipEntity.getId());
        if (betSlipEntity.getPicks() != null) {
            for (MatchEventPickEntity p : betSlipEntity.getPicks()) {
                betSlipDomain.addMatchEventPick(toMatchEventDomain(p));
            }
        }
        return betSlipDomain;
    }

    public MatchEventPick toMatchEventDomain(MatchEventPickEntity p) {
        var matchEventPickDomain = new MatchEventPick(p.getMatchKey(), p.outcomeName(), p.getOdd());
        matchEventPickDomain.setId(p.getId());
        return matchEventPickDomain;
    }

    public MobileMoneyAccount toMobileMoneyDomain(MobileMoneyAccountsEntity m) {
        var domainMomo = new MobileMoneyAccount(m.accountType);
        domainMomo.setAccountBalance(m.getAccountBalance());
        domainMomo.setDailyLimit(m.getDailyLimit());
        domainMomo.setMonthlyLimit(m.getMonthlyLimit());
        domainMomo.setWeeklyLimit(m.getWeeklyLimit());
        if (m.getTransactionHistory() != null) {
            for (TransactionEntity t : m.getTransactionHistory()) {
                domainMomo.addTransaction(toTransactionDomain(t));
            }
        }
        return domainMomo;
    }
    public List<BettingAccount> toListOfAccountDomains(List<BettingAccountEntity> list){
        return list.stream().map(this::toBettingAccountDomain).collect(Collectors.toCollection(ArrayList::new));
    }

}
