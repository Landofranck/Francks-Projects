package project.adapter.out.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import project.adapter.out.persistence.EntityModels.MobileMoneyAccountsEntity;
import project.adapter.out.persistence.Mappers.MomoAccountMapper;
import project.application.port.out.mobilMoney.*;
import project.domain.model.Enums.BrokerType;
import project.domain.model.MobileMoneyAccount;
import project.domain.model.Transaction;

import java.util.List;
import java.util.Objects;


@ApplicationScoped
public class MomoAccountRepositoryJpa implements PersistMobileMoneyAccount, ReadAllMomoAccounts, ReadMomoAccountByIdPort, UpdateMobileMoneyBalancePort, AppendMobileMoneyTransactionPort {
    @Inject
    EntityManager entityManager;
    @Inject
    MomoAccountMapper mapper;

    private boolean existsByNameAndType(String name, BrokerType type) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(b) FROM BettingAccountEntity b WHERE b.accountName = :name AND b.brokerType = :type",
                        Long.class
                )
                .setParameter("name", name)
                .setParameter("type", type)
                .getSingleResult();

        return count > 0;
    }
    @Transactional
    @Override
    public Long saveMomoAccountToDataBase(MobileMoneyAccount account) {
        Objects.requireNonNull(account);
        var entity = mapper.toMobileMoneyEntity(account);
        try {
            entityManager.persist(entity);
            entityManager.flush();
            return entity.getId();

        } catch (Exception e) {
            throw new IllegalArgumentException("error while persisting MobileMoneyAccount:" + e.getMessage());
        }
    }

    @Override
    public List<MobileMoneyAccount> getAllMomoAccounts() {

        List<MobileMoneyAccountsEntity> entities = entityManager.
                createQuery("SELECT d FROM MobileMoneyAccountsEntity d", MobileMoneyAccountsEntity.class).getResultList();
        List<MobileMoneyAccount> accounts = mapper.toListOfMOMOtDomains(entities);
        return accounts;

    }

    @Override
    public MobileMoneyAccount getMomoAccount(Long id) {
        var entity = entityManager.find(MobileMoneyAccountsEntity.class, id);
        if (entity == null) throw new NotFoundException("Momo account not found: " + id);
        return mapper.toMobileMoneyDomain(entity);
    }

    @Transactional
    @Override
    public void updateBalance(MobileMoneyAccount account) {
        var entity = entityManager.find(MobileMoneyAccountsEntity.class, account.getAccountId());
        if (entity == null) throw new IllegalArgumentException("Momo account not found: " + account.getAccountId());

        entity.setAccountBalance(account.getAccountBalance().getValue());
        entity.setDailyLimit(account.getDailyLimit());
        entity.setWeeklyLimit(account.getWeeklyLimit());
        entity.setMonthlyLimit(account.getMonthlyLimit());
    }
    @Transactional
    @Override
    public void appendToMobileMoney(Long momoAccountId, Transaction transaction) {
        var owner = entityManager.find(MobileMoneyAccountsEntity.class, momoAccountId);
        if (owner == null) throw new IllegalArgumentException("Momo account not found: " + momoAccountId);

        var txEntity = mapper.toMomoTransactionEntity(transaction);
        txEntity.setOwner(owner); // whatever the field is called
        entityManager.persist(txEntity);

        owner.getTransactionHistory().add(txEntity); // optional if bidirectional
    }
}
