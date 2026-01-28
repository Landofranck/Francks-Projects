package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.adapter.out.persistence.NotFoundException;
import project.application.port.in.Reducer.PlaceBetFromReducerUseCase;
import project.application.port.out.GetReducerByIdPort;
import project.application.port.out.UpdateReducerPort;
import project.application.service.betSlip.MakeBetUseCaseImpl;
import project.domain.model.MatchEventPick;
import project.domain.model.Money;
import project.domain.model.Reducer.Reducer;
import project.domain.model.Reducer.ReducerBetSlip;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PlaceBetFromReducerUseCaseImpl implements PlaceBetFromReducerUseCase {

    @Inject
    GetReducerByIdPort getReducerById;
    @Inject
    MakeBetUseCaseImpl makeBetUseCase;
    @Inject
    UpdateReducerPort updateReducer;

    @Override
    public Reducer placeBetFromReducer(Long reducerId, Long bettingId, int slipNumber, Money stake) {
        var red = getReducerById.getReducer(reducerId);

        if (slipNumber > red.getSlips().size())
            throw new NotFoundException("Slip number is out of range placebetfrom..impl 34");
        if ( red.getSlips().isEmpty()||red.getSlips()==null)
            throw new NotFoundException("there are no slips in the reducer you are using placebetfrom..impl 34");

        List<Long> matchIds = new ArrayList<>();
        if(red.getSlips().get(0).getPicks().get(0).getIdentity()==null){throw new RuntimeException("id i s af");}
        for (MatchEventPick r : red.getSlips().get(slipNumber).getPicks()) {
            matchIds.add(r.getIdentity());
        }

        List<String> outCome=new ArrayList<>();
        for (MatchEventPick r : red.getSlips().get(slipNumber).getPicks()) {
            outCome.add(r.getOutcomeName());
        }

        red.getSlips().get(slipNumber).placeParBet(stake);
        var betStake=(stake==null)? red.getSlips().get(slipNumber).getPlanedStake():stake;
        makeBetUseCase.makeBet(bettingId,matchIds,outCome,betStake,"reducer bet: "+reducerId);
        return updateReducer.updateReducer(reducerId,red);

    }
}
