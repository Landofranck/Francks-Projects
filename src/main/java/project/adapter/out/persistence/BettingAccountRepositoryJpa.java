package project.adapter.out.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import project.adapter.out.persistence.EntityModels.AccountEntity;
import project.adapter.out.persistence.EntityModels.BettingAccountEntity;
import project.adapter.out.persistence.EntityModels.Mapper;
import project.adapter.out.persistence.EntityModels.MobileMoneyAccountsEntity;
import project.application.port.out.*;
import project.domain.model.Account;
import project.domain.model.BettingAccount;
import project.domain.model.MobileMoneyAccount;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class BettingAccountRepositoryJpa implements PersistBettingAccountPort, ReadAccountByIdPort, PersistMobileMoneyAccount, ReadAllBettingAccountsPort, ReadAllMomoAccounts {
    @Inject
    EntityManager entityManager;
    @Inject
    Mapper mapper;

    @Transactional
    @Override
    public Long saveB(BettingAccount account) {
        Objects.requireNonNull(account);
        entityManager.persist(mapper.toBettingAccountEntity(account));
        return account.getAccountId();
    }

    @Override
    public BettingAccount getAccount(Long id) {
        var entity = entityManager.find(BettingAccountEntity.class, id);
        var output = mapper.toBettingAccountDomain(entity);
        return output;
    }

    @Transactional
    @Override
    public long saveAccountToDataBase(MobileMoneyAccount account) {
        Objects.requireNonNull(account);
        entityManager.persist(mapper.toMobileMoneyEntity(account));
        return account.getAccountId();
    }

    @Transactional
    @Override
    public List<BettingAccount> getAllBettingAcounts() {
        try {
            List<BettingAccountEntity> entities = entityManager
                    .createQuery("SELECT d FROM BettingAccountEntity d", BettingAccountEntity.class).getResultList();

             List<BettingAccount> accounts=mapper.toListOfAccountDomains(entities);
             return accounts;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<MobileMoneyAccount> getAllMomoAccounts() {
        try {
            List<MobileMoneyAccountsEntity> entities=entityManager
        }
    }
}
