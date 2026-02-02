package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.RefreshReducerUseCase;
import project.application.port.out.Reducer.GetReducerByIdPort;
import project.application.port.out.Reducer.RefreshReducerByIdPort;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Reducer.Reducer;

import java.util.Objects;

@ApplicationScoped
public class RefreshReducerUseCaseImpl implements RefreshReducerUseCase {
    @Inject
    GetReducerByIdPort getReducer;
    @Inject
    RefreshReducerByIdPort refreshReducer;

    @Override
    public Reducer refreshReducerById(Long reducerId) {
        var refreshed = getReducer.getReducer(reducerId);
        if (Objects.equals(refreshed.getMatchVersion(), refreshed.updateMatchVersion())) {
            return refreshed;
        } else {
            refreshed.computeSlips(BetCategory.SINGLE);
            return refreshReducer.refreshReducer(reducerId, refreshed);
        }
    }
}