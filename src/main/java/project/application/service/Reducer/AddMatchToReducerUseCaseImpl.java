package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.AddMatchToReducerUseCase;
import project.application.port.out.GetReducerByIdPort;
import project.application.port.out.Match.ReadMatchByIdPort;
import project.application.port.out.PersistReducerPort;
import project.domain.model.Match;
import project.domain.model.Reducer;
@ApplicationScoped
public class AddMatchToReducerUseCaseImpl implements AddMatchToReducerUseCase {
    @Inject
    GetReducerByIdPort getReducer;
    @Inject
    PersistReducerPort persistReducer;
    @Inject
    ReadMatchByIdPort readMatch;

    @Override
    public Reducer addMatchToReducer(Long reducerId, Long in) {
        var reducer=getReducer.getReducer(reducerId);
        var match=readMatch.getMatch(in);
        reducer.addMatches(match);
        persistReducer.persistReducerToDataBase(reducer);
        return reducer;
    }
}
