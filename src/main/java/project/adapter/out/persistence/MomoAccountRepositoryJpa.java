package project.adapter.out.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import project.adapter.in.web.Utils.Code;
import project.adapter.out.persistence.EntityModels.MomoEntites.MobileMoneyAccountsEntity;
import project.adapter.out.persistence.EntityModels.MomoEntites.MomoAccountTransactionEntity;
import project.adapter.out.persistence.Mappers.MomoAccountMapper;
import project.application.error.ConflictException;
import project.application.error.ResourceNotFoundException;
import project.application.error.ValidationException;
import project.application.port.out.mobilMoney.*;
import project.domain.model.Enums.TransactionType;
import project.domain.model.MobileMoneyAccount;
import project.domain.model.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@ApplicationScoped
public class MomoAccountRepositoryJpa implements PersistMobileMoneyAccount, ReadAllMomoAccounts, ReadMomoAccountByIdPort, UpdateMobileMoneyBalancePort, AppendMobileMoneyTransactionPort, ReadMomoTransactionHistoryPort,ReadSummaryMomoAccountPort {
    @Inject
    EntityManager entityManager;
    @Inject
    MomoAccountMapper mapper;

    @Transactional
    @Override
    public Long saveMomoAccountToDataBase(MobileMoneyAccount account) {
        Objects.requireNonNull(account);
        var entity = mapper.toMobileMoneyEntity(account);
        try {
            entityManager.persist(entity);
            entityManager.flush();
            return entity.getId();

        } catch (ConstraintViolationException e) {
            throw new ConflictException(Code.MOMO_ACCOUNT_ALREADY_EXISTS,"error while persisting MobileMoneyAccount JPA 43:" + e.getMessage(),Map.of("momoId",entity.getId()));
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
    public MobileMoneyAccount getSummaryMomoAccount(Long id) {
        var entity = entityManager.find(MobileMoneyAccountsEntity.class, id);
        if (entity == null) throw new ResourceNotFoundException(Code.MOMO_ACCOUNT_NOT_FOUND,"MomoAccountRepositoryJpa 67", Map.of("momoId",id));
        return mapper.toSummaryMobileMoneyDomain(entity);
    }
    @Override
    public MobileMoneyAccount getMomoAccount(Long id) {
        var entity = entityManager.find(MobileMoneyAccountsEntity.class, id);
        if (entity == null) throw new ResourceNotFoundException(Code.MOMO_ACCOUNT_NOT_FOUND,"MomoAccountRepositoryJpa 74", Map.of("momoId",id));
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

    @Override
    public List<Transaction> readMomoTransactions(Long momoId, TransactionType type) {

        try{StringBuilder jpql = new StringBuilder(
                "SELECT b FROM MomoAccountTransactionEntity b WHERE b.owner.id = :momoId"
        );

        if (type != null) {
            jpql.append(" AND b.type = :type");
        }

        jpql.append(" ORDER BY b.createdAt DESC");

        var query = entityManager.createQuery(jpql.toString(), MomoAccountTransactionEntity.class)
                .setParameter("momoId", momoId);

        if (type != null) {
            query.setParameter("type",type );
        }



        var transactionHistory = query.getResultList();
        return mapper.toMomoTransactionHistory(transactionHistory);} catch (IllegalArgumentException e) {
            throw new ValidationException(Code.MOMO_ACCOUNT_ERROR,"Error while getting momo transactions momo..jpa 117"+e.getMessage(),Map.of());
        }
    }
}
