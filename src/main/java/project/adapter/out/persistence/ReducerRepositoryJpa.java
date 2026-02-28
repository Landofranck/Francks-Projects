package project.adapter.out.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import project.adapter.in.web.Reducer.ReducerDto.ComputeDto;
import project.adapter.in.web.Reducer.ReducerDto.ReadReducerSummaryDto;
import project.adapter.in.web.Utils.Code;
import project.adapter.out.persistence.EntityModels.BettingAccount.MatchEntity;
import project.adapter.out.persistence.EntityModels.ReducerEntity;
import project.adapter.out.persistence.EntityModels.ReducerSummaryEntity;
import project.adapter.out.persistence.Mappers.ReducerMapper;
import project.application.error.ConflictException;
import project.application.error.ResourceNotFoundException;
import project.application.port.out.PersistReducerSummaryPort;
import project.application.port.out.Reducer.*;
import project.domain.model.Enums.BrokerType;
import project.domain.model.Money;
import project.domain.model.Reducer.Reducer;
import project.domain.model.Reducer.ReducerSummary;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ReducerRepositoryJpa implements PersistReducerPort, DeleteReducerSummaryPort, ReadReducerByIdPort, UpdateReducerPort, AddMatchToReducerPort, DeleteMatchFromReducerPort, RefreshReducerByIdPort, DeleteReducerByIdPort, ReadAllReducersPort, UpdateReducerStakePort, ReadSummaryReducerPort, PersistReducerSummaryPort, ReadAllReducerSummaryPort, AddReducerToSummaryPort {
    @Inject
    EntityManager entityManager;
    @Inject
    ReducerMapper mapper;

    @Transactional
    @Override
    public Long persistReducerToDataBase(Reducer reducer) {
        try {
            var entity = mapper.toReducerEntity(reducer);
            entityManager.persist(entity);
            entityManager.flush();
            return entity.getId();
        } catch (ConstraintViolationException e) {
            throw new ConflictException(Code.REDUCER_ALREADY_EXIST, e.getMessage() + " reducer repository jpa 35", Map.of("reducerId", null));
        }
    }

    @Transactional
    @Override
    public Reducer readReducer(Long id) {
        var entity = entityManager.find(ReducerEntity.class, id);
        if (entity == null)
            throw new ResourceNotFoundException(Code.REDUCER_NOT_FOUND, "ReducerRepositoryJpa 35", Map.of("reducerId", id));
        var out = mapper.toReducerDomain(entity);
        entityManager.flush();
        entityManager.clear();
        return out;
    }

    @Transactional
    @Override
    public Reducer updateReducer(Long reducerId, Reducer update) {
        var oldReducer = entityManager.find(ReducerEntity.class, reducerId);

        if (oldReducer == null)
            throw new ResourceNotFoundException(Code.REDUCER_NOT_FOUND, "ReducerRepositoryJpa 49", Map.of("reducerId", reducerId));
        mapper.applyChangesToReducer(oldReducer, update);
        entityManager.flush();
        entityManager.clear();
        var val = entityManager.find(ReducerEntity.class, reducerId);


        return mapper.toReducerDomain(val);
    }

    @Transactional
    @Override
    public Reducer addMatchToReducer(Long reducerId, Long matchId) {
        var reducer = entityManager.find(ReducerEntity.class, reducerId);
        var match = entityManager.find(MatchEntity.class, matchId);
        if (reducer == null)
            throw new ResourceNotFoundException(Code.REDUCER_NOT_FOUND, "ReducerRepositoryJpa 65", Map.of("ReducerId", reducerId));
        if (match == null)
            throw new ResourceNotFoundException(Code.MATCH_NOT_FOUND, "ReducerRepositoryJpa 69", Map.of("ReducerId", matchId));
        reducer.addMatches(match);
        entityManager.flush();
        entityManager.clear();
        var out = entityManager.find(ReducerEntity.class, reducerId);
        return mapper.toReducerDomain(out);
    }

    @Transactional
    @Override
    public void deleteMatch(Long reducerId, Long matchId) {
        var reducer = entityManager.find(ReducerEntity.class, reducerId);
        if (reducer == null)
            throw new ResourceNotFoundException(Code.REDUCER_NOT_FOUND, "ReducerRepositoryJpa 82", Map.of("ReducerId", reducerId));
        var match = entityManager.find(MatchEntity.class, matchId);
        if (match == null)
            throw new ResourceNotFoundException(Code.MATCH_NOT_FOUND, "ReducerRepositoryJpa 85", Map.of("ReducerId", matchId));

        reducer.deleteMatch(match);
    }

    @Transactional
    @Override
    public Reducer refreshReducer(Long id, Reducer refreshed) {
        var entity = entityManager.find(ReducerEntity.class, id);
        if (entity == null)
            throw new ResourceNotFoundException(Code.REDUCER_NOT_FOUND, "ReducerRepositoryJpa 94", Map.of("ReducerId", id));
        mapper.refreshChangesReducer(entity, refreshed);
        entityManager.flush();
        entityManager.clear();
        var out = entityManager.find(ReducerEntity.class, id);
        return mapper.toReducerDomain(out);
    }

    @Transactional
    @Override
    public void deleteReducerById(Long id) {
        var entity = entityManager.find(ReducerEntity.class, id);
        if (entity == null)
            throw new ResourceNotFoundException(Code.REDUCER_NOT_FOUND, "ReducerRepositoryJpa 106", Map.of("ReducerId", id));
        entityManager.remove(entity);
    }

    @Override
    public List<Reducer> readReducers(BrokerType broker) {
        StringBuilder jpql = new StringBuilder(
                "SELECT b FROM ReducerEntity b"
        );

        if (broker != null) {
            jpql.append(" WHERE b.broker = :broker");
        }

        var query = entityManager.createQuery(jpql.toString(), ReducerEntity.class);

        if (broker != null) {
            query.setParameter("broker", broker);
        }


        var reducers = query.getResultList();
        return mapper.toReducerDomains(reducers);

    }

    @Transactional
    @Override
    public void updateStake(Long id, Money stake, Money bonusStake) {
        var out = entityManager.find(ReducerEntity.class, id);
        if (out == null) {
            throw new ResourceNotFoundException(Code.REDUCER_NOT_FOUND, "Reducer with id " + id + " not fond Reducer jpa...149", Map.of());
        }
        out.setTotalStake(stake.getValue());
        if (!bonusStake.getValue().equals(BigDecimal.ZERO))
            out.setBonusAmount(bonusStake.getValue());
    }

    @Transactional
    @Override
    public ReducerSummary getSummaryById(Long summaryId) {
        var reducerSummary = entityManager.find(ReducerSummaryEntity.class, summaryId);
        if (reducerSummary == null) {
            throw new ResourceNotFoundException(Code.REDUCER_NOT_FOUND, "ReducerSummary with id does not exist : jpa 164" + summaryId, Map.of());
        }
        return mapper.toReducerSummaryDomain(reducerSummary);
    }

    @Transactional
    @Override
    public Long persistReducerSummary(ReducerSummary summary) {
        try {
            var entity = mapper.toReducerSummaryEntity(summary);
            entityManager.persist(entity);
            entityManager.flush();
            entityManager.clear();
            return entity.getReducerSummaryId();
        } catch (Exception e) {
            throw new RuntimeException("error while persisting reducer Summary: jpa 180 " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void deleteReducerSummary(Long summaryId) {
        try {
            var summary = entityManager.find(ReducerSummaryEntity.class, summaryId);
            if (summary == null)
                throw new ResourceNotFoundException(Code.REDUCER_NOT_FOUND, "ReducerSummary with id does not exist : jpa 189" + summaryId, Map.of());
            entityManager.flush();
            entityManager.remove(summary);
        } catch (Exception e) {
            throw new RuntimeException("error while deleting reducer Summary: jpa 192 " + e.getMessage());
        }
    }

    @Override
    public List<ReducerSummary> readReducerSummaries() {
        var out = entityManager.createQuery("SELECT b FROM ReducerSummaryEntity b", ReducerSummaryEntity.class).getResultList();
        return mapper.toReducerSummaryEntityList(out);
    }

    @Transactional
    @Override
    public void addReducerToSummary(Long summaryId, Long reducerId) {
        var summary=entityManager.find(ReducerSummaryEntity.class, summaryId);
        if (summary==null)
            throw new ResourceNotFoundException(Code.REDUCER_SUMMARY_NOT_FOUND,"The reducer summary with id "+summaryId+" does not exist reducerjpa...210",Map.of());
        var reducer=entityManager.find(ReducerEntity.class, reducerId);
        if (reducer==null)
            throw new ResourceNotFoundException(Code.REDUCER_NOT_FOUND,"The reducer with id "+summaryId+" does not exist reducerjpa...213",Map.of());
        summary.addReducerEntity(reducer);
        entityManager.flush();
    }
}
