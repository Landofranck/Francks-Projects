package project.adapter.out.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.QueryParameterException;
import org.hibernate.exception.ConstraintViolationException;
import project.adapter.in.web.Utils.Code;
import project.adapter.out.persistence.EntityModels.BettingAccount.*;
import project.adapter.out.persistence.EntityModels.BettingAccount.MatchOutcomeEntity;
import project.adapter.out.persistence.Mappers.BettingAccountMapper;
import project.application.error.ConflictException;
import project.application.error.ResourceNotFoundException;
import project.application.error.ValidationException;
import project.application.port.out.DeleteMatchByIdPort;
import project.application.port.out.Match.*;
import project.application.port.out.UpdateMatchPort;
import project.application.port.out.bettingAccount.*;
import project.domain.model.*;
import project.domain.model.Enums.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ApplicationScoped
public class BettingAccountRepositoryJpa implements PersistBettingAccountPort, ReadBettingAccountByIdPort, ReadSummaryOfBettingAccountsPort, ReadTransactionHistoryPort, ReadBettingHistoryPort, ReadAllBettingAccountsPort, UpdateBettingAccountBalancePort, AppendBettingAccountTransactionPort, PersistMatchPort, ReadAllMatchesPort, PersistBetSlipToAccountPort, PersistDraftBetSlipPort, ReadEmptSlipByParenPort, DeleteMatchByIdPort, UpdateMatchPort, UpdateBettingAccountPort, UpdateSlipPickStatusPort, FindMatchOutComeByParametersPort, ReadBetSlipByIdPort,ReadMatchByIdPort, SetSlipStatustoRefundPort, UpdateMatchBonusPort, FindSlipEventOutComeByParamPort, UpdateMatchPickStatusPort {
    @Inject
    EntityManager entityManager;
    @Inject
    BettingAccountMapper mapper;


    @Transactional
    @Override
    public Long saveBettingAccount(BettingAccount account) {
        try {
            Objects.requireNonNull(account);
            var entity = mapper.toBettingAccountEntity(account);

            entityManager.persist(entity);
            entityManager.flush();
            return entity.getId();
        } catch (ConstraintViolationException e) {
            entityManager.clear();
            Long id = entityManager.createQuery("""
                    SELECT r.id FROM BettingAccountEntity r WHERE LOWER(r.accountName)=LOWER(:name)
                    """, Long.class).setParameter("name", account.getAccountName()).getSingleResult();
            throw new ConflictException(Code.BETTING_ACCOUNT_ALREADY_EXISTS, "betting account repository jpa 48: " + e.getMessage(), Map.of("bettingId", id, "bettingName", account.getAccountName()));
        }
    }

    @Override
    public BettingAccount getBettingAccount(Long id) {
        var entity = entityManager.find(BettingAccountEntity.class, id);
        if (entity == null)
            throw new ResourceNotFoundException(Code.BETTING_ACCOUNT_NOT_FOUND, "BAR jpa 56", Map.of("bettingId", id));
        return mapper.toBettingAccountDomain(entity);
    }

    @Override
    public BettingAccount getSummaryAccount(Long id) {
        var entity = entityManager.find(BettingAccountEntity.class, id);
        if (entity == null)
            throw new ResourceNotFoundException(Code.BETTING_ACCOUNT_NOT_FOUND, "BAR jpa 56", Map.of("bettingId", id));
        return mapper.toSummaryBettingAccountDomain(entity);
    }

    @Override
    public List<BetSlip> readBetHistory(Long bettingId, BetStatus status, String matchKey, BetStrategy strategy) {

        StringBuilder jpql = new StringBuilder("SELECT b FROM BetSlipEntity b WHERE b.parentAccountEntity.id = :bettingId");

        if (status != null) {
            jpql.append(" AND b.status = :status");
        }

        if (strategy != null) {
            jpql.append(" AND b.strategy = :strategy");
        }

        jpql.append(" ORDER BY b.createdAt DESC");

        var query = entityManager.createQuery(jpql.toString(), BetSlipEntity.class).setParameter("bettingId", bettingId);

        if (status != null) {
            query.setParameter("status", status);
        }

        if (strategy != null) {
            query.setParameter("strategy", strategy);
        }

        var betHistory = query.getResultList();
        return mapper.toListOfBetSlips(betHistory);
    }

    @Override
    public List<Transaction> readTransactionHistory(Long bettingId, TransactionType type) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT r FROM BettingAccountTransactionEntity r WHERE r.owner.id = :bettingId");
            if (type != null) {
                jpql.append(" AND r.type = :type");
            }
            jpql.append(" ORDER BY r.createdAt DESC");
            var query = entityManager.createQuery(jpql.toString(), BettingAccountTransactionEntity.class).setParameter("bettingId", bettingId);

            if (type != null) {
                query.setParameter("type", type);
            }

            return mapper.toListOfBettingAccountTransactions(query.getResultList());
        } catch (IllegalArgumentException e) {
            throw new ValidationException(Code.BETTING_ACCOUNT_ERROR, "Error whils getting transaction history jpa 120 " + e.getMessage(), Map.of());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Transactional
    @Override
    public List<BettingAccount> getAllBettingAcounts(BrokerType brokerType) {

        try {
            StringBuilder sb = new StringBuilder();
            if (brokerType != null) {
                sb.append("WHERE d.brokerType= :brokerType");
            }
            var query = entityManager.createQuery("SELECT d FROM BettingAccountEntity d " + sb.toString(), BettingAccountEntity.class);
            if (brokerType != null) {
                query.setParameter("brokerType", brokerType);
            }
            List<BettingAccountEntity> entities = query.getResultList();
            return mapper.toListOfAccountDomains(entities);

        } catch (IllegalArgumentException e) {
            throw new ValidationException(Code.BETTING_ACCOUNT_ERROR, "error while returning list of all betting accounts jpa 132: " + e.getMessage(), Map.of());

        } catch (Exception e) {
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


    @Transactional
    @Override
    public List<Match> getAllMatches(BrokerType broker, Long id, String name, Instant start, Instant stop) {
        try {

            StringBuilder jpql = new StringBuilder("SELECT m FROM MatchEntity m");
            List<String> conditions = new ArrayList<>();

            if (broker != null) {
                conditions.add("m.broker = :broker");
            }
            if (id != null) {
                conditions.add("m.id = :id");
            }
            if (start != null) {
                conditions.add("m.begins >= :start");
            }
            if (stop != null) {
                conditions.add("m.begins <= :stop");
            }
            if (name != null && !name.isBlank()) {
                // Match either home or away; use LIKE for partial match
                conditions.add("(LOWER(m.home) LIKE :name OR LOWER(m.away) LIKE :name)");
            }

            if (!conditions.isEmpty()) {
                jpql.append(" WHERE ").append(String.join(" AND ", conditions));
            }

            jpql.append(" ORDER BY m.begins DESC");

            var query = entityManager.createQuery(jpql.toString(), MatchEntity.class);

            if (broker != null) query.setParameter("broker", broker);
            if (id != null) query.setParameter("id", id);
            if (start != null) query.setParameter("start", start);
            if (stop != null) query.setParameter("stop", stop);
            if (name != null && !name.isBlank()) query.setParameter("name", "%" + name.toLowerCase() + "%");
            return mapper.toMatchDomains(query.getResultList());

        } catch (IllegalArgumentException e) {
            throw new ValidationException(
                    Code.MATCH_ERROR,
                    e.getClass() + " error while getting all matches bettingAccount repo 215 " + e.getMessage(),
                    Map.of()
            );
        }
    }

    @Transactional
    @Override
    public Long persistSlipToAccount(Long bettingAccountId, DraftBetSlip slip) {
        var owner = entityManager.find(BettingAccountEntity.class, bettingAccountId);
        if (owner == null)
            throw new ResourceNotFoundException(Code.BETTING_ACCOUNT_NOT_FOUND,"Betting account not found: bettingAccountJpa 155 id" + bettingAccountId,Map.of("bettingId",bettingAccountId));

        var slipEntity = mapper.fromDraftToBetslipEntity(slip);

        owner.addBetSlipEntity(slipEntity);     // ✅ sets parentAccountEntity
        entityManager.persist(owner);
        entityManager.flush();
         return slipEntity.getId();
    }

    @Transactional
    @Override
    public void persistDraftSlip(Long bettingAccountId, DraftBetSlip betSlip) {
         try {

             var emptySlipOwner = entityManager.find(BettingAccountEntity.class, bettingAccountId);
            if (emptySlipOwner == null) {
                throw new IllegalArgumentException("Betting account not found jpa line 222: " + bettingAccountId);
            }
            var managedDraft = emptySlipOwner.getDraftBetSlip();
            if (managedDraft == null) {
                managedDraft = new DraftSlipEntity();
                managedDraft.setDraftBetSlipOwner(emptySlipOwner); // owning side of @OneToOne
                emptySlipOwner.setDraftBetSlip(managedDraft);
            }

            mapper.applyToManagedDraftSlip(managedDraft, betSlip);
             entityManager.flush();
            entityManager.clear();
        } catch (Exception e) {
            throw new ValidationException(Code.INTERNAL_ERROR,"error persisting new empty sip jpa line 227 " + e.getMessage(),Map.of());
        }
    }

    @Override
    public DraftBetSlip getAvailableBettingSlip(Long parentAccountId) {
        var parentAccount = entityManager.find(BettingAccountEntity.class, parentAccountId);
        if (parentAccount == null)
            throw new ResourceNotFoundException(Code.BETTING_ACCOUNT_NOT_FOUND, "Betting account with id " + parentAccountId + "not found jpa line 231: ", Map.of());
        var slip = mapper.toDraftSlipDomain(parentAccount.getDraftBetSlip());
        if (slip == null)
            throw new ResourceNotFoundException(Code.BET_SLIP_NOT_FOUND, "Betting account " + parentAccount.getAccountName() + " does not have this and empty slip : " + parentAccountId, Map.of());
        return slip;
    }

    @Transactional
    @Override
    public void deleteMatchById(Long id) {
        MatchEntity match = entityManager.find(MatchEntity.class, id);
        if (match == null) {
            throw new ResourceNotFoundException(Code.MATCH_NOT_FOUND, "BettingAccountRepositoryJpa 231", Map.of("matchIds", id));
        }

        // IMPORTANT: remove from the OWNING side (ReducerEntity.betMatchEntities)
        for (var reducer : List.copyOf(match.getReducers())) {
            reducer.deleteMatch(match); // this removes from reducer.betMatchEntities and from match.reducers
        }

        entityManager.flush(); // ensure join table rows are deleted first
        entityManager.remove(match);
    }

    @Transactional
    @Override
    public void updateMatch(Long id, List<MatchOutComePick> in) {
        var out = entityManager.find(MatchEntity.class, id);
        if (out == null)
            throw new ResourceNotFoundException(Code.MATCH_NOT_FOUND, "BettingAccountRepositoryJpa 247", Map.of("matchIds", id));
        mapper.applyToMatchEntity(out, in);
        entityManager.flush();
        entityManager.clear();
        mapper.toMatchDomain(out);
    }

    @Override
    public Match readMatch(Long id) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT r FROM MatchEntity  r WHERE");
            if (id != null) {
                jpql.append(" r.id= :id");
            }

            var query = entityManager.createQuery(jpql.toString(), MatchEntity.class);
            if (id != null) {
                query.setParameter("id", id);
            }
            var out = query.getSingleResult();
            return mapper.toMatchDomain(out);
        } catch (Exception e) {
            if (e.getClass() == QueryParameterException.class || e.getClass() == IllegalArgumentException.class) {
                throw new ValidationException(Code.MATCH_ERROR, "error while retrieving matches jpa 288: " + e.getClass() + " " + e.getMessage(), Map.of());
            }
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateBettingAccount(Long bettingId, BettingAccount updated) {
        var oldVersion = entityManager.find(BettingAccountEntity.class, bettingId);
        if (oldVersion == null) {
            throw new ResourceNotFoundException(Code.BETTING_ACCOUNT_NOT_FOUND, "betting account not found with id " + bettingId + ", jpa 346", Map.of("bettingId", bettingId));
        }

        mapper.applyTobettingAccount(oldVersion, updated);
    }

    @Transactional
    @Override
    public List<MatchOutComePick> findSlipEventEntity(String ownerName, MatchKey matchKey, String outcomeName, League outComePickLeague) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT r FROM SlipEventPickEntity r WHERE LOWER(r.ownerMatchName)=LOWER(:ownerName) AND r.matchKey=:matchKey");
            if (outcomeName != null && !outcomeName.isBlank()) {
                jpql.append(" AND LOWER(r.outcomeName)=LOWER(:outcomeName)");
            }
            if (outComePickLeague != null) {
                jpql.append(" AND r.league=(:league)");
            }
            var query = entityManager.createQuery(jpql.toString(), SlipEventPickEntity.class).setParameter("matchKey", matchKey).setParameter("ownerName", ownerName);

            if (outcomeName != null && !outcomeName.isBlank()) {
                query.setParameter("outcomeName", outcomeName);
            }
            if (outComePickLeague != null) {
                query.setParameter("league", outComePickLeague);

            }
            return mapper.toListOfSlipOutComePick(query.getResultList());
        } catch (Exception e) {
            throw new ValidationException(Code.MATCH_ERROR, "error while retrieving all match outcome picks from data base jpa...365: " + e.getMessage(), Map.of());
        }
    }

    @Transactional
    @Override
    public List<MatchOutComePick> findMatchOutComes(String ownerName, MatchKey matchKey, String outcomeName, League outComePickLeague) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT r FROM MatchOutcomeEntity r WHERE LOWER(r.ownerMatchName)=LOWER(:ownerName) AND r.matchKey=:matchKey");
            if (outcomeName != null && !outcomeName.isBlank()) {
                jpql.append(" AND LOWER(r.outcomeName)=LOWER(:outcomeName)");
            }
            if (outComePickLeague != null) {
                jpql.append(" AND r.league=(:league)");
            }
            var query = entityManager.createQuery(jpql.toString(), MatchOutcomeEntity.class).setParameter("matchKey", matchKey).setParameter("ownerName", ownerName);

            if (outcomeName != null && !outcomeName.isBlank()) {
                query.setParameter("outcomeName", outcomeName);
            }
            if (outComePickLeague != null) {
                query.setParameter("league", outComePickLeague);

            }
            return mapper.toListOfMatchOutComePick(query.getResultList());
        } catch (Exception e) {
            throw new ValidationException(Code.MATCH_ERROR, "error while retrieving all match outcome picks from data base jpa...365: " + e.getMessage(), Map.of());
        }
    }

    @Transactional
    @Override
    public Long updateMatchPick(Long matchPickId, BetStatus newPickStatus) {
        var out = entityManager.find(MatchOutcomeEntity.class, matchPickId);
        if (out == null)
            throw new ResourceNotFoundException(Code.BET_SLIP_NOT_FOUND, "BettingAccountRepositoryJpa 291", Map.of("matchIds", matchPickId));
        out.setOutcomePickStatus(newPickStatus);
        entityManager.flush();
        entityManager.clear();
        return out.getParentMatch().getId();
    }

    @Override
    public BetSlip readBetSlip(Long id) {
        var out = entityManager.find(BetSlipEntity.class, id);
        return mapper.toBetSlipDomain(out);
    }

    @Transactional
    @Override
    public Long setSlipStatusToRefund(Long id) {
        var out = entityManager.find(BetSlipEntity.class, id);
        if (out.getBonusSlip()) throw new IllegalArgumentException("you cannot refund bonus slips jpa 275");
        out.setStatus(BetStatus.REFUNDED);
        return out.getParentAccountEntity().getId();
    }

    @Transactional
    @Override
    public void updateMatchBonus(Long matchId) {
        var managedMatch = entityManager.find(MatchEntity.class, matchId);
        managedMatch.setBonusMatch(!managedMatch.isBonusMatch());
    }

    @Transactional
    @Override
    public Long updateSlipPick(Long matchPickId, BetStatus newPickStatus) {
        var out = entityManager.find(SlipEventPickEntity.class, matchPickId);
        if (out == null)
            throw new ResourceNotFoundException(Code.BET_SLIP_NOT_FOUND, "BettingAccountRepositoryJpa 291", Map.of("matchIds", matchPickId));

        out.setOutComePickStatus(newPickStatus);

        entityManager.flush();
        entityManager.clear();

        return out.getParentBetSlipEntity().getParentAccountEntity().getId();
    }
}