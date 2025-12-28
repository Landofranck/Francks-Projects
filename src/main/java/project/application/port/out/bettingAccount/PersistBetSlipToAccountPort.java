package project.application.port.out.bettingAccount;

public interface PersistBetSlipToAccountPort {
    Long persistBetSlip(Long bettingAccountId, project.domain.model.BetSlip slip);
}
