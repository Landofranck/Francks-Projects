package project.adapter.out.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import project.adapter.out.persistence.EntityModels.BettingAccountEntity;
import project.adapter.out.persistence.EntityModels.Mapper;
import project.adapter.out.persistence.EntityModels.MatchEntity;
import project.adapter.out.persistence.EntityModels.MobileMoneyAccountsEntity;
import project.application.port.out.*;
import project.application.port.out.Match.PersistMatchPort;
import project.application.port.out.Match.ReadAllMatchesPort;
import project.application.port.out.Match.ReadMatchByIdPort;
import project.application.port.out.bettingAccount.*;
import project.application.port.out.mobilMoney.*;
import project.domain.model.BettingAccount;
import project.domain.model.Match;
import project.domain.model.MobileMoneyAccount;
import project.domain.model.Transaction;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class BettingAccountRepositoryJpa implements PersistBettingAccountPort, ReadAccountByIdPort, PersistMobileMoneyAccount, ReadAllBettingAccountsPort, ReadAllMomoAccounts,
        ReadMomoAccountByIdPort, UpdateBettingAccountBalancePort, UpdateMobileMoneyBalancePort, AppendBettingAccountTransactionPort,
        AppendMobileMoneyTransactionPort , PersistMatchPort, ReadMatchByIdPort, ReadAllMatchesPort, PersistBetSlipToAccountPort
{
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
        } catch (EntityExistsException e) {
            throw new EntityExistsException("error while persisting bankAccount", e);
        } catch (Exception e) {
            throw new RuntimeException("error while persisting bankAccount", e);
        }
    }

    @Override
    public BettingAccount getAccount(Long id) {
        var entity = entityManager.find(BettingAccountEntity.class, id);
        if (entity == null) throw new NotFoundException("Account not found: " + id);
        var output = mapper.toBettingAccountDomain(entity);
        return output;
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
        } catch (EntityExistsException e) {
            throw new EntityExistsException("error while persisting MobileMoneyAccount", e);
        } catch (Exception e) {
            throw new RuntimeException("error while persisting MobileMoneyAccount", e);
        }
    }

    @Transactional
    @Override
    public List<BettingAccount> getAllBettingAcounts() {

        List<BettingAccountEntity> entities = entityManager
                .createQuery("SELECT d FROM BettingAccountEntity d", BettingAccountEntity.class).getResultList();

        List<BettingAccount> accounts = mapper.toListOfAccountDomains(entities);

        return accounts;

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
    public void updateBalance(BettingAccount account) {
        var entity = entityManager.find(BettingAccountEntity.class, account.getAccountId());
        if (entity == null) throw new IllegalArgumentException("Betting account not found: " + account.getAccountId());

        entity.setBalance(account.getBalance().getValue()); // only update balance
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
    public void appendToBettingAccount(Long bettingAccountId, Transaction transaction) {
        var owner = entityManager.find(BettingAccountEntity.class, bettingAccountId);
        if (owner == null) throw new IllegalArgumentException("Betting account not found: " + bettingAccountId);

        var txEntity = mapper.toBettingAccountTransactionEntity(transaction);
        txEntity.setOwner(owner); // or owner field name
        entityManager.persist(txEntity);

        // Optional: if you maintain bidirectional list in entity:
        owner.getTransactionHistory().add(txEntity);
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
    @Transactional
    @Override
    public Long saveMatch(Match match) {
        var entity = mapper.toMatchEntity(match);
        entityManager.persist(entity);
        entityManager.flush();
        return entity.getId();
    }

    @Override
    public Match getMatch(Long id) {
        var entity = entityManager.find(MatchEntity.class, id);
        if (entity == null) throw new IllegalArgumentException("Match not found: " + id);
        return mapper.toMatchDomain(entity);
    }

    @Override
    public List<Match> getAllMatches() {
        var entities = entityManager
                .createQuery("SELECT m FROM MatchEntity m", MatchEntity.class)
                .getResultList();
        return mapper.toMatchDomains(entities);
    }
    @Transactional
    @Override
    public Long persistSlipToAccount(Long bettingAccountId, project.domain.model.BetSlip slip) {
        var owner = entityManager.find(BettingAccountEntity.class, bettingAccountId);
        if (owner == null) throw new IllegalArgumentException("Betting account not found: " + bettingAccountId);

        var slipEntity = mapper.toBetslipEntity(slip);

        owner.addBetSlipEntity(slipEntity);     // ✅ sets parentAccountEntity
        entityManager.persist(slipEntity);
        entityManager.flush();

        return slipEntity.getId();
    }


}
