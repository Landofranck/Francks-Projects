package project.adapter.out.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import project.adapter.out.persistence.EntityModels.ReducerEntity;
import project.adapter.out.persistence.Mappers.ReducerMapper;
import project.application.port.out.GetReducerByIdPort;
import project.application.port.out.PersistReducerPort;
import project.domain.model.Reducer;

@ApplicationScoped
public class ReducerRepositoryJpa implements PersistReducerPort, GetReducerByIdPort {
    @Inject
    EntityManager entityManager;
    @Inject
    ReducerMapper mapper;

    @Transactional
    @Override
    public void persistReducerToDataBase(Reducer reducer) {
        var entity = mapper.toReducerEntity(reducer);
        entityManager.persist(entity);
    }
    @Override
    public Reducer getReducer(Long id) {
       var entity= entityManager.find(ReducerEntity.class, id);
       var out=mapper.toReducerDomain(entity);
       return out;
    }
}
