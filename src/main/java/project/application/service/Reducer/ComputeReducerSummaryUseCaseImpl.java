package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.*;
import project.application.port.out.Reducer.*;
import project.domain.model.Money;
import project.domain.model.Reducer.Block;
import project.domain.model.Reducer.Reducer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ComputeReducerSummaryUseCaseImpl implements ComuteReducerSummaryUseCase, AddReducerToSummaryUseCase, RemoveReducerFromSummaryUseCase {
    @Inject
    ComputeCombinationUseCase computeCombinationUseCase;
    @Inject
    ReadSummaryReducerPort readSummaryReducer;
    @Inject
    AddReducerToSummaryPort addReducerToSummaryPort;
    @Inject
    RemoveReducerFromSummaryPort removeReducerFromSummary;
    @Inject
    UpdateReducerSummaryBalancePort updateReducerSummaryBalance;
    @Inject
    UpdateReducerStakeUseCase updateReducerStake;

    @Override
    public void comuteReducerSummary(Long summaryId, List<Block> blocks, BigDecimal balance) {
        updateReducerSummaryBalance.updateReducerSummary(summaryId,new Money(balance) );
        var sm = readSummaryReducer.getSummaryById(summaryId);
        var reducerIds = new ArrayList<Long>();
        for (Reducer r : sm.getReducers()) {
            reducerIds.add(r.getId());
        }
        for (Long reducerId : reducerIds) {
            updateReducerStake.updateReducerStake(reducerId,balance,null);
            computeCombinationUseCase.computeCombination(reducerId, blocks);
        }

    }

    @Override
    public void addReducerToSummary(Long summaryId, Long reducerId) {
        addReducerToSummaryPort.addReducerToSummary(summaryId, reducerId);
    }

    @Override
    public void removeReducerFromSummary(Long summaryId, Long reducerId) {
        removeReducerFromSummary.removeReducerFromSummary(summaryId, reducerId);
    }
}
