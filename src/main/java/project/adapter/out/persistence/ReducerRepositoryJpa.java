package project.adapter.out.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import project.adapter.out.persistence.EntityModels.BettingAccount.MatchEntity;
import project.adapter.out.persistence.EntityModels.ReducerEntity;
import project.adapter.out.persistence.Mappers.ReducerMapper;
import project.application.port.out.*;
import project.domain.model.Reducer.Reducer;

@ApplicationScoped
public class ReducerRepositoryJpa implements PersistReducerPort, GetReducerByIdPort, UpdateReducerPort, AddMatchToReducerPort, DeleteMatchFromReducerPort {
    @Inject
    EntityManager entityManager;
    @Inject
    ReducerMapper mapper;

    @Transactional
    @Override
    public Long persistReducerToDataBase(Reducer reducer) {
        var entity = mapper.toReducerEntity(reducer);
        entityManager.persist(entity);
        entityManager.flush();
        return entity.getId();
    }

    @Transactional
    @Override
    public Reducer getReducer(Long id) {
        var entity = entityManager.find(ReducerEntity.class, id);
        if (entity == null)
            throw new NotFoundException("Reducer with Id " + id + " was not found");
        var out = mapper.toReducerDomain(entity);
        return out;
    }

    @Transactional
    @Override
    public Reducer updateReducer(Long reducerId, Reducer update) {
        var oldReducer = entityManager.find(ReducerEntity.class, reducerId);

        if (oldReducer == null)
            throw new NotFoundException("Reducer with id " + reducerId + "not found");
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
        if (reducer == null || match == null) throw new NotFoundException("enter valid Id's");
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
        if (reducer == null) throw new NotFoundException("Redcuer with id " + reducerId + " not found");

        var match = entityManager.find(MatchEntity.class, matchId);
        if (match == null) throw new NotFoundException("match with id " + matchId + " not found");

        reducer.deleteMatch(match);
    }

}
