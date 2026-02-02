package project.application.service.Reducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.Reducer.DeleteReducerByIdUsecase;
import project.application.port.out.Reducer.DeleteReducerByIdPort;

@ApplicationScoped
public class DeleteReducerById implements DeleteReducerByIdUsecase {
    @Inject
    DeleteReducerByIdPort deleteReducerById;

    @Override
    public void deleteRecuder(Long id) {
        deleteReducerById.deleteReducerById(id);
    }
}
