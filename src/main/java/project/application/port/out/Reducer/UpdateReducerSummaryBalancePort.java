package project.application.port.out.Reducer;

import project.domain.model.Money;

public interface UpdateReducerSummaryBalancePort {
    void updateReducerSummary(Long summaryId, Money newBalance);
}
