package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.LoadSummaryReducerUseCase;
import project.application.port.out.Reducer.ReadReducerByIdPort;
import project.application.port.out.Reducer.ReadSummaryReducerPort;
import project.domain.model.Reducer.ReducerSummary;

@ApplicationScoped
public class GetSummaryReducerUseCaseImpl implements LoadSummaryReducerUseCase {
    @Inject
    ReadSummaryReducerPort readSummaryReducer;

    @Override
    public ReducerSummary loadReducer(Long summaryId) {
        var reducerSummary = readSummaryReducer.getSummaryById(summaryId);
        reducerSummary.placeMatches();
            reducerSummary.computeSummaryReducerSlips();
            return reducerSummary;
    }
}
