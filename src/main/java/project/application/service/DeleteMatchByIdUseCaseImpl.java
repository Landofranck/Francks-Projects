package project.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.DeleteMatchByIdUsecase;
import project.application.port.out.DeleteMatchByIdPort;
import project.application.port.out.GetReducerByIdPort;

@ApplicationScoped
public class DeleteMatchByIdUseCaseImpl implements DeleteMatchByIdUsecase {
    @Inject
    DeleteMatchByIdPort deleteMatchById;

    @Override
    public void deleteMatchById(Long id) {
        deleteMatchById.deleteMatchById(id);
    }
}
