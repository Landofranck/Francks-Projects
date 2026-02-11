package project.application.service.betSlip;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import project.application.port.in.betSlip.SetBetSlipToRefundUseCase;
import project.application.port.out.bettingAccount.*;
import project.domain.model.Enums.BetStatus;

import java.util.InputMismatchException;

@ApplicationScoped
public class SetBetSlipToRefundUseCaseImpl implements SetBetSlipToRefundUseCase {

    @Inject
    SetSlipStatustoRefundPort refundPort;
    @Inject
    ReadBettingAccountByIdPort readBettingAccount;
    @Inject
    UpdateBettingAccountBalancePort updateBalance;
    @Inject
    AppendBettingAccountTransactionPort appendTx;
    @Inject
    GetBetSlipByIdPort getBetSlipById;

    @Inject
    UpdateBettingAccountPort updateBettingAccount;

    @Override
    public void setSlipToRefund(Long betAccountId, Long slipId) {
        var b = getBetSlipById.getBetSlip(slipId);
        if(b.getStatus()!= BetStatus.PENDING)
            throw new IllegalArgumentException("you cannot set this slip to refunded, it has already concluded: setsliprefund 32");
        Long bettingAccountId = refundPort.setSlipStatusToRefund(slipId);
        if (bettingAccountId != betAccountId) {
            throw new InputMismatchException("Bet account with id " + bettingAccountId + " doesn't have bet slip with id " + slipId + ": setBetslipRefund 31");
        }
        var account = readBettingAccount.getBettingAccount(bettingAccountId);

            var tx = account.betRefundedTransaction(b.getStake(), b.getStrategy().toString());

            updateBalance.updateBalance(account);
            appendTx.appendToBettingAccount(bettingAccountId, tx);
            updateBettingAccount.updateBettingAccount(bettingAccountId, account);

    }
}
