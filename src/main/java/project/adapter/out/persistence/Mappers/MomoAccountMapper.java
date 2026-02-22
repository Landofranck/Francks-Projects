package project.adapter.out.persistence.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import project.adapter.out.persistence.EntityModels.BettingAccount.BetSlipEntity;
import project.adapter.out.persistence.EntityModels.MomoEntites.MobileMoneyAccountsEntity;
import project.adapter.out.persistence.EntityModels.MomoEntites.MomoAccountTransactionEntity;
import project.domain.model.MobileMoneyAccount;
import project.domain.model.Money;
import project.domain.model.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MomoAccountMapper {
    public MomoAccountTransactionEntity toMomoTransactionEntity(Transaction domainTransaction) {
        var transactionEntity = new MomoAccountTransactionEntity();
        transactionEntity.setAccountBalanceAfterTransaction(domainTransaction.getAccountBalanceAfterTransaction().getValue());
        transactionEntity.setTransactionAmmount(domainTransaction.getTransactionAmmount().getValue());
        transactionEntity.setCreatedAt(domainTransaction.getCreatedAt());
        transactionEntity.setDescription(domainTransaction.getDescription());
        transactionEntity.setType(domainTransaction.getType());
        //set owner not created because this is done in parent class already with setParent(this)
        return transactionEntity;
    }
    public MobileMoneyAccountsEntity toMobileMoneyEntity(MobileMoneyAccount momo) {
        var entitMomo = new MobileMoneyAccountsEntity();
        entitMomo.setId(momo.getAccountId());
        entitMomo.setAccountBalance(momo.getAccountBalance().getValue());
        entitMomo.setAccountType(momo.getAccountType());
        entitMomo.setName(momo.getName());
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
    public Transaction toMomoTransactionDomain(MomoAccountTransactionEntity e) {
        var transacttionDomain = new Transaction(new Money(e.getTransactionAmmount()), new Money(e.getAccountBalanceAfterTransaction()), e.getCreatedAt(), e.getType(), e.getDescription(),null);
        //set owner not created because this is done in parent class already with setParent(this)
        transacttionDomain.setId(e.getId());
        return transacttionDomain;
    }
    public MobileMoneyAccount toMobileMoneyDomain(MobileMoneyAccountsEntity m) {
        var domainMomo=toMomo(m);
        if (m.getTransactionHistory() != null) {
            for (MomoAccountTransactionEntity t : m.getTransactionHistory()) {
                domainMomo.addTransaction(toMomoTransactionDomain(t));
            }
        }
        return domainMomo;
    }

    public MobileMoneyAccount toSummaryMobileMoneyDomain(MobileMoneyAccountsEntity m) {
        return toMomo(m);
    }

    private MobileMoneyAccount toMomo(MobileMoneyAccountsEntity m) {
        var domainMomo = new MobileMoneyAccount(m.getId(), m.getAccountType(),m.getName());
        domainMomo.setId(m.getId());
        domainMomo.setAccountBalance(new Money(m.getAccountBalance()));
        domainMomo.setDailyLimit(m.getDailyLimit());
        domainMomo.setMonthlyLimit(m.getMonthlyLimit());
        domainMomo.setWeeklyLimit(m.getWeeklyLimit());
        return domainMomo;
    }

    public List<MobileMoneyAccount> toListOfMOMOtDomains(List<MobileMoneyAccountsEntity> list) {
        return list.stream().map(this::toMobileMoneyDomain).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Transaction> toMomoTransactionHistory(List<MomoAccountTransactionEntity> transactionHistory) {
return transactionHistory.stream().map(this::toMomoTransactionDomain).collect(Collectors.toCollection(ArrayList::new));
    }
}
