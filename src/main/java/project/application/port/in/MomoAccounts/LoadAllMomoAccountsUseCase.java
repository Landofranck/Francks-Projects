package project.application.port.in.MomoAccounts;

import project.domain.model.MobileMoneyAccount;
import java.util.List;

public interface LoadAllMomoAccountsUseCase {
    List<MobileMoneyAccount> getAllMomoAccounts();
}
