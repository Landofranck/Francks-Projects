package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.DeleteReducerSummaryUseCase;
import project.application.port.out.Reducer.DeleteReducerSummaryPort;

@ApplicationScoped
public class DeleteSummaryReducerUseCaseImpl implements DeleteReducerSummaryUseCase {
    @Inject
    DeleteReducerSummaryPort deleteReducerSummaryPort;

    @Override
    public void deleteReducerSummary(Long summaryId) {
        deleteReducerSummaryPort.deleteReducerSummary(summaryId);
    }
}
