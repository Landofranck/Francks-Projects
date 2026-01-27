package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.AddMatchToReducerUseCase;
import project.application.port.out.AddMatchToReducerPort;
import project.application.port.out.GetReducerByIdPort;
import project.application.port.out.Match.ReadMatchByIdPort;
import project.application.port.out.UpdateReducerPort;
import project.domain.model.Reducer;
@ApplicationScoped
public class AddMatchToReducerUseCaseImpl implements AddMatchToReducerUseCase {

    @Inject
    AddMatchToReducerPort updateReducer;

    @Override
    public Reducer addMatchToReducer(Long reducerId, Long in) {
    var out=updateReducer.addMatchToReducer(reducerId,in);
    return out;
    }
}
