package project.adapter.out.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import project.adapter.in.web.Utils.Code;
import project.adapter.out.persistence.EntityModels.BettingAccount.MatchEntity;
import project.adapter.out.persistence.EntityModels.ReducerEntity;
import project.adapter.out.persistence.Mappers.ReducerMapper;
import project.application.error.ConflictException;
import project.application.error.ResourceNotFoundException;
import project.application.port.out.Reducer.*;
import project.domain.model.Enums.BrokerType;
import project.domain.model.Money;
import project.domain.model.Reducer.Reducer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ReducerRepositoryJpa implements PersistReducerPort, GetReducerByIdPort, UpdateReducerPort, AddMatchToReducerPort, DeleteMatchFromReducerPort, RefreshReducerByIdPort, DeleteReducerByIdPort, GetAllReducersPort, UpdateReducerStakePort {
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
    public Reducer getReducer(Long id) {
        var entity = entityManager.find(ReducerEntity.class, id);
        if (entity == null)
            throw new ResourceNotFoundException(Code.REDUCER_NOT_FOUND, "ReducerRepositoryJpa 35", Map.of("ReducerId", id));
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
            throw new ResourceNotFoundException(Code.REDUCER_NOT_FOUND, "ReducerRepositoryJpa 49", Map.of("ReducerId", reducerId));
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
    public List<Reducer> getReducers(BrokerType broker) {
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
}
