package project.application.port.in;

import project.domain.model.Account;

import java.util.List;

public interface LoadAllAccountsUseCase {
    public List<Account> loadAllAccounts();
}
