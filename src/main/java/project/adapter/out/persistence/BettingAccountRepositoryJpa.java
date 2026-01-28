package project.adapter.out.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import project.adapter.out.persistence.EntityModels.BettingAccount.BettingAccountEntity;
import project.adapter.out.persistence.Mappers.BettingAccountMapper;
import project.adapter.out.persistence.EntityModels.BettingAccount.MatchEntity;
import project.application.port.out.Match.PersistMatchPort;
import project.application.port.out.Match.ReadAllMatchesPort;
import project.application.port.out.Match.ReadMatchByIdPort;
import project.application.port.out.bettingAccount.*;
import project.domain.model.*;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BrokerType;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class BettingAccountRepositoryJpa implements PersistBettingAccountPort, ReadBettingAccountByIdPort, ReadAllBettingAccountsPort,
         UpdateBettingAccountBalancePort, AppendBettingAccountTransactionPort,
         PersistMatchPort, ReadMatchByIdPort, ReadAllMatchesPort, PersistBetSlipToAccountPort, PersistEmptyBetSlipPort,
        ReadEmptSlipByParenPort{
    @Inject
    EntityManager entityManager;
    @Inject
    BettingAccountMapper mapper;

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
    public Long saveBettingAccount(BettingAccount account) {


        Objects.requireNonNull(account);

        var entity = mapper.toBettingAccountEntity(account);
        if (existsByNameAndType(entity.getAccountName(), entity.getBrokerType())) {
            throw new IllegalArgumentException(
                    "Betting account with name '" + entity.getAccountName() +
                            "' already exists for broker " + entity.getBrokerType()
            );
        }

        entityManager.persist(entity);

        entityManager.flush();
        return entity.getId();
    }

    @Override
    public BettingAccount getBettingAccount(Long id) {
        var entity = entityManager.find(BettingAccountEntity.class, id);
        if (entity == null) throw new NotFoundException("Account not found: jpa 68. With Id:" + id);
        var output = mapper.toBettingAccountDomain(entity);
        return output;
    }



    @Transactional
    @Override
    public List<BettingAccount> getAllBettingAcounts() {

        try{
        List<BettingAccountEntity> entities = entityManager
                .createQuery("SELECT d FROM BettingAccountEntity d", BettingAccountEntity.class).getResultList();

        List<BettingAccount> accounts = mapper.toListOfAccountDomains(entities);


        return accounts;
        }catch (Exception e){
            throw new RuntimeException("error while returning list of all betting accounts jpa 101");
        }

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
    public Long saveMatch(Match match) {
        if (match.getMatchOutComes() == null || match.getMatchOutComes().isEmpty())
            throw new IllegalArgumentException("you need some outcomes BAR 172");
        var entity = mapper.toMatchEntity(match);
        if (match.getMatchOutComes() == null || match.getMatchOutComes().isEmpty())
            throw new IllegalArgumentException("you need some outcomes BAR 174");
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

    @Transactional
    @Override
    public List<Match> getAllMatches() {
        var entities = entityManager
                .createQuery("SELECT DISTINCT m FROM MatchEntity m LEFT JOIN FETCH m.matchOutComes", MatchEntity.class)
                .getResultList();
        if (entities.isEmpty())
            throw new IllegalArgumentException("no matches found in the database repositoryJpa 192");
        List<Match> matches = mapper.toMatchDomains(entities);
        return matches;
    }

    @Transactional
    @Override
    public Long persistSlipToAccount(Long bettingAccountId, DraftBetSlip slip, String Strategy) {
        var owner = entityManager.find(BettingAccountEntity.class, bettingAccountId);
        if (owner == null) throw new IllegalArgumentException("Betting account not found: " + bettingAccountId);

        var slipEntity = mapper.fromDraftToBetslipEntity(slip);
        slipEntity.setCategory(BetCategory.SINGLE);

        owner.addBetSlipEntity(slipEntity);     // ✅ sets parentAccountEntity
        entityManager.persist(owner);
        entityManager.flush();

        return slipEntity.getId();
    }

    @Transactional
    @Override
    public DraftBetSlip persistEmptyslip(Long bettingAccountId, DraftBetSlip betSlip) {
        try {
            var emptySlipOwner = entityManager.find(BettingAccountEntity.class, bettingAccountId);
            if (emptySlipOwner == null)
                throw new IllegalArgumentException("Betting account not found jpa line 222: " + bettingAccountId);
            var slipEntity = mapper.toDraftSlipEntity(betSlip);
            emptySlipOwner.putNewBetSlip(slipEntity);
            entityManager.persist(emptySlipOwner);
            return mapper.toDraftSlipDomain(emptySlipOwner.getDraftBetSlip());
        } catch (Exception e) {
            throw new IllegalArgumentException("erron persisting new empty sip jpa line 227 " + e.getMessage());
        }
    }

    @Override
    public DraftBetSlip getAvailableBettingSlip(Long parentAccountId) {
        var parentAccount = entityManager.find(BettingAccountEntity.class, parentAccountId);
        if (parentAccount == null)
            throw new IllegalArgumentException("Betting account not found jpa line 231: " + parentAccountId);
        var slip = mapper.toDraftSlipDomain(parentAccount.getDraftBetSlip());
        if (slip == null)
            throw new IllegalArgumentException("Betting account " + parentAccount.getAccountName()
                    + " does not have this and empty slip : " + parentAccountId);
        return slip;
    }
}
