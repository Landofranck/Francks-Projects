package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.UpdateReducerShuffleComboUseCase;
import project.application.port.out.Reducer.ReadReducerByIdPort;
import project.application.port.out.Reducer.UpdateReducerPort;
import project.domain.model.Reducer.Reducer;

@ApplicationScoped
public class UpdateReducerShuffleComboUseCaseImpl implements UpdateReducerShuffleComboUseCase {
    @Inject
    ReadReducerByIdPort readReducer;
    @Inject
    UpdateReducerPort updateReducerPort;

    @Override
    public void updateShuffleComboInReducer(Long reducerId) {
        var out = readReducer.readReducer(reducerId);
        out.checkSchuffle();
        updateReducerPort.updateReducer(reducerId, out);
    }
}
