package project.adapter.out.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import project.adapter.out.persistence.EntityModels.BettingAccountEntity;
import project.adapter.out.persistence.EntityModels.Mapper;
import project.adapter.out.persistence.EntityModels.MobileMoneyAccountsEntity;
import project.application.port.out.*;
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
    public Long saveBettingAccount(BettingAccount account) {
        try {
            Objects.requireNonNull(account);
            var entity = mapper.toBettingAccountEntity(account);
            entityManager.persist(entity);
            entityManager.flush();
            return entity.getId();
        } catch (Exception e) {
            throw new RuntimeException("error while persisting bankAccount" + e.getMessage());
        }
    }

    @Override
    public BettingAccount getAccount(Long id) {
        var entity = entityManager.find(BettingAccountEntity.class, id);
        if (entity==null)throw new IllegalArgumentException("Account not found: "+ id);
        var output = mapper.toBettingAccountDomain(entity);
        return output;
    }

    @Transactional
    @Override
    public Long saveMomoAccountToDataBase(MobileMoneyAccount account) {
        Objects.requireNonNull(account);
        var entity =mapper.toMobileMoneyEntity(account);
        entityManager.persist(entity);
        entityManager.flush();
        return entity.getId();
    }

    @Transactional
    @Override
    public List<BettingAccount> getAllBettingAcounts() {
        try {
            List<BettingAccountEntity> entities = entityManager
                    .createQuery("SELECT d FROM BettingAccountEntity d", BettingAccountEntity.class).getResultList();

            List<BettingAccount> accounts = mapper.toListOfAccountDomains(entities);
            return accounts;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<MobileMoneyAccount> getAllMomoAccounts() {
        try {
            List<MobileMoneyAccountsEntity> entities = entityManager.
                    createQuery("SELECT d FROM MobileMoneyAccountsEntity d", MobileMoneyAccountsEntity.class).getResultList();
            List<MobileMoneyAccount> accounts = mapper.toListOfMOMOtDomains(entities);
            return accounts;
        } catch (Exception e) {
            throw new RuntimeException("error, while getting all accounts" + e.getMessage());
        }
    }
}
