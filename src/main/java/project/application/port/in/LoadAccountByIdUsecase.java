package project.application.port.in;

import project.domain.model.Account;

public interface LoadAccountByIdUsecase {
    public Account loadAccout(Long id);
}
