package project.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import project.application.port.in.LoadAllMomoAccountsUseCase;
import project.application.port.out.mobilMoney.ReadAllMomoAccounts;
import project.domain.model.MobileMoneyAccount;

import java.util.List;

@ApplicationScoped
public class GetAllMoMoAccountsUseCaseImpl implements LoadAllMomoAccountsUseCase {

    @Inject
    ReadAllMomoAccounts loadAllM;



    @Override
    public List<MobileMoneyAccount> getAllMomoAccounts() {
        return loadAllM.getAllMomoAccounts();
    }
}
