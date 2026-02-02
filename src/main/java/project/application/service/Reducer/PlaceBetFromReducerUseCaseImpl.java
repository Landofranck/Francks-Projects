package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.adapter.out.persistence.NotFoundException;
import project.application.port.in.Reducer.PlaceBetFromReducerUseCase;
import project.application.port.out.Reducer.GetReducerByIdPort;
import project.application.port.out.Reducer.UpdateReducerPort;
import project.application.port.out.bettingAccount.ReadBettingAccountByIdPort;
import project.application.service.betSlip.MakeBetUseCaseImpl;
import project.domain.model.MatchOutComePick;
import project.domain.model.Money;
import project.domain.model.Reducer.Reducer;

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
    @Inject
    ReadBettingAccountByIdPort readBettingAccount;

    @Override
    public Reducer placeBetFromReducer(Long reducerId, Long bettingAccountId, int slipNumber, Money stake, Integer bonusSlip) {
        var red = getReducerById.getReducer(reducerId);
        var test= (bonusSlip == null || bonusSlip<0);
        if (stake.equals(new Money(0)) && (test)) {
            throw new IllegalArgumentException("you have to put a stake or pick a bonus slip");
        } else if (stake.isGreaterOrEqual(new Money(1)) && (bonusSlip != null)) {
            throw new IllegalArgumentException("you have to put a stake or pick a bonus slip but not both");
        }
        if (slipNumber > red.getSlips().size())
            throw new NotFoundException("Slip number is out of range placebetfrom..impl 34");
        if (red.getSlips().isEmpty() || red.getSlips() == null)
            throw new NotFoundException("there are no slips in the reducer you are using place bet from..impl 34");


        List<Long> matchIds = new ArrayList<>();
        for (MatchOutComePick r : red.getSlips().get(slipNumber).getPicks()) {
            matchIds.add(r.getIdentity());
        }


        var acc=readBettingAccount.getBettingAccount(bettingAccountId);
        List<String> outCome = new ArrayList<>();
        for (MatchOutComePick r : red.getSlips().get(slipNumber).getPicks()) {
            outCome.add(r.getOutcomeName());
        }


        var betStake=(!test)? acc.getBonuses().get(bonusSlip).getAmount():stake;
        red.getSlips().get(slipNumber).placeParBet(betStake,!test);
        makeBetUseCase.makeBet(bettingAccountId, matchIds, outCome, stake, red.getStrategy(), bonusSlip);

        return updateReducer.updateReducer(reducerId, red);
    }
}
