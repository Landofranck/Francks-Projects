package project.adapter.out.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import project.adapter.in.web.Utils.Code;
import project.adapter.out.persistence.EntityModels.BettingAccount.*;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.io.FileDescriptor.out;

@ApplicationScoped
public class BettingAccountRepositoryJpa implements PersistBettingAccountPort, ReadBettingAccountByIdPort, ReadSummaryOfBettingAccountsPort, ReadTransactionHistoryPort, ReadBettingHistoryPort, ReadAllBettingAccountsPort, UpdateBettingAccountBalancePort, AppendBettingAccountTransactionPort, PersistMatchPort, ReadMatchByIdPort, ReadAllMatchesPort, PersistBetSlipToAccountPort, PersistEmptyBetSlipPort, ReadEmptSlipByParenPort, DeleteMatchByIdPort, UpdateMatchPort, GetMatchByIdPort, UpdateBettingAccountPort, UpdateMatchPickStatusPort, FindMatchOutComeByParametersPort, GetBetSlipByIdPort, SetSlipStatustoRefundPort {
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
            StringBuilder sb=new StringBuilder();
            if(brokerType!=null){
                sb.append("WHERE d.brokerType= :brokerType");
            }
            var query = entityManager.createQuery("SELECT d FROM BettingAccountEntity d "+sb.toString(), BettingAccountEntity.class);
            if(brokerType!=null){
                query.setParameter("brokerType",brokerType);
            }
            List<BettingAccountEntity> entities=query.getResultList();
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

    @Override
    public Match getMatch(Long id) {
        var entity = entityManager.find(MatchEntity.class, id);
        if (entity == null) throw new IllegalArgumentException("Match not found: " + id);
        return mapper.toMatchDomain(entity);
    }

    @Transactional
    @Override
    public List<Match> getAllMatches(BrokerType broker, Long id, String name, Instant start, Instant stop) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT m FROM MatchEntity m WHERE m.broker= :broker");
            if (id != null) {
                jpql.append(" AND m.id = :id");
            }
            if (start != null) {
                jpql.append(" AND m.begins >= :start");
            }
            if (stop != null) {
                jpql.append(" AND m.ends <= :stop");
            }
            if (name != null) {
                jpql.append(" OR m.home <= :name OR m.home <= :name");
            }


            jpql.append(" ORDER by b.begins DESC");


            var query = entityManager.createQuery(jpql.toString(), MatchEntity.class).setParameter("broker", broker);
            if (id != null) {
                query.setParameter("id", id);
            }
            if (start != null) {
                query.setParameter("start", start);
            }
            if (stop != null) {
                query.setParameter("stop", stop);
            }
            if (name != null) {
                query.setParameter("name",name);
            }
            return mapper.toMatchDomains(query.getResultList());
        } catch (IllegalArgumentException e) {
            throw new ValidationException(Code.MATCH_ERROR, "error while getting all matches bettingAccount repo 215 " + e.getMessage(), Map.of());
        }
    }

    @Transactional
    @Override
    public Long persistSlipToAccount(Long bettingAccountId, DraftBetSlip slip, BetStrategy strategy) {
        var owner = entityManager.find(BettingAccountEntity.class, bettingAccountId);
        if (owner == null)
            throw new IllegalArgumentException("Betting account not found: bettingAccountJpa 155 id" + bettingAccountId);

        var slipEntity = mapper.fromDraftToBetslipEntity(slip);

        slipEntity.setStrategy(strategy);
        owner.addBetSlipEntity(slipEntity);     // ✅ sets parentAccountEntity
        entityManager.persist(owner);
        entityManager.flush();
        return slipEntity.getId();
    }

    @Transactional
    @Override
    public void persistEmptySlip(Long bettingAccountId, DraftBetSlip betSlip) {
        try {
            var emptySlipOwner = entityManager.find(BettingAccountEntity.class, bettingAccountId);
            if (emptySlipOwner == null)
                throw new IllegalArgumentException("Betting account not found jpa line 222: " + bettingAccountId);
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
            throw new IllegalArgumentException("erron persisting new empty sip jpa line 227 " + e.getMessage());
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
            throw new ResourceNotFoundException(Code.MATCH_NOT_FOUND, "BettingAccountRepositoryJpa 231", Map.of("matchId", id));
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
            throw new ResourceNotFoundException(Code.MATCH_NOT_FOUND, "BettingAccountRepositoryJpa 247", Map.of("matchId", id));
        mapper.applyToMatchEntity(out, in);
        entityManager.flush();
        entityManager.clear();
        mapper.toMatchDomain(out);
    }

    @Override
    public Match getMatchByIdOrName(Long id, BrokerType broker, String name) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT r FROM MatchEntity  r WHERE");
            if (broker != null) {
                jpql.append(" r.broker=:broker");
            }
            if (id != null) {
                jpql.append(" AND r.id= :id");
            }
            if (name != null) {
                jpql.append(" OR r.home= :id OR r.away= :name");
            }
            jpql.append(" ORDER BY b.begins DESC");


            var query = entityManager.createQuery(jpql.toString(), MatchEntity.class);
            if (id != null) {
                query.setParameter("id", id);
            }
            if (broker != null) {
                query.setParameter("broker", broker);
            }
            if (name != null) {
                query.setParameter("name", name);
            }
            var out = query.getSingleResult();
            return mapper.toMatchDomain(out);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(Code.MATCH_ERROR, "error while retrieving matches jpa 288", Map.of());
        }
    }

    @Transactional
    @Override
    public void updateBettingAccount(Long bettingId, BettingAccount updated) {
        var oldVersion = entityManager.find(BettingAccountEntity.class, bettingId);
        mapper.applyTobettingAccount(oldVersion, updated);

        entityManager.flush();
        entityManager.clear();

        var acc = entityManager.find(BettingAccountEntity.class, bettingId);
        if (true) {
            throw new RuntimeException("this is the error" + acc.getDraftBetSlip().getPicks().size());
        }
    }

    @Transactional
    @Override
    public List<MatchOutComePick> findMatchOutComes(String matchKey, String outcomeName, League outComePickLeague) {
        List<MatchEventPickEntity> matchEventPicks = entityManager.createQuery("SELECT r FROM MatchEventPickEntity r WHERE LOWER(r.matchKey)=LOWER(:matchKey) AND LOWER(r.outcomeName)=LOWER(:outcomeName) AND r.league=(:league)", MatchEventPickEntity.class).setParameter("matchKey", matchKey).setParameter("outcomeName", outcomeName).setParameter("league", outComePickLeague).getResultList();
        return mapper.toListOfMatchOutComePick(matchEventPicks);
    }

    @Transactional
    @Override
    public Long updateMatchPick(Long matchPickId, BetStatus newPickStatus) {
        var out = entityManager.find(MatchEventPickEntity.class, matchPickId);
        if (out == null)
            throw new ResourceNotFoundException(Code.BET_SLIP_NOT_FOUND, "BettingAccountRepositoryJpa 291", Map.of("matchId", matchPickId));
        out.setOutComePickStatus(newPickStatus);
        entityManager.flush();
        entityManager.clear();
        return out.getParentBetSlipEntity().getParentAccountEntity().getId();
    }

    @Override
    public BetSlip getBetSlip(Long id) {
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
}