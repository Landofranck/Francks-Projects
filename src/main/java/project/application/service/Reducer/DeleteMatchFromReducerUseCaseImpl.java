package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.ClearAllMatchesFromReducerUseCase;
import project.application.port.in.Reducer.DeleteMatchFromReducerUseCase;
import project.application.port.out.Reducer.DeleteMatchFromReducerPort;
import project.application.port.out.Reducer.ReadReducerByIdPort;
import project.domain.model.Match;

import java.util.ArrayList;

@ApplicationScoped
public class DeleteMatchFromReducerUseCaseImpl implements DeleteMatchFromReducerUseCase, ClearAllMatchesFromReducerUseCase {
    @Inject
    DeleteMatchFromReducerPort deleteMatchFromReducer;
    @Inject
    ReadReducerByIdPort readReducerByIdPort;

    @Override
    public void deletMatchFromReducer(Long reducerId, Long matchId) {
        deleteMatchFromReducer.deleteMatch(reducerId, matchId);
    }
    @Override
    public void clearAllMatchesFromReducer(Long reducerId) {
       var red= readReducerByIdPort.readReducer(reducerId);
       var ids=new ArrayList<Long>();
       for (Match m:red.getBetMatches()){
           ids.add(m.getMatchId());
       }
       for (Long id:ids){
           deleteMatchFromReducer.deleteMatch(reducerId,id);
       }
    }
}
