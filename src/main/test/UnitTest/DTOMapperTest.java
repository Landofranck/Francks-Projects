package UnitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.adapter.in.web.DTOMapper;
import project.adapter.in.web.MatchDto;
import project.adapter.in.web.TransactionDTO.ReadReducerDto;
import project.adapter.in.web.bettinAccountDTO.CreateBettingAccountDto;
import project.adapter.in.web.bettinAccountDTO.BettingAccountDto;
import project.adapter.in.web.bettinAccountDTO.betslip.BetSlipDto;
import project.adapter.in.web.MobileMoneyDto.CreateMobileMoneyAccountDto;
import project.adapter.in.web.MobileMoneyDto.ReadMomoAccountDto;
import project.domain.model.*;
import project.domain.model.Enums.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DTOMapperTest {

    private DTOMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DTOMapper();
    }

    @Test
    void toBettingAccountDomain_requiresNonNullFields() {
        var dto = new CreateBettingAccountDto();

        // dto null
        assertThrows(NullPointerException.class, () -> mapper.toBettingAccountDomain(null));

        // accountName null
        dto.setAccountName(null);
        dto.setBrokerType(BrokerType.BET365);
        assertThrows(NullPointerException.class, () -> mapper.toBettingAccountDomain(dto));

        // brokerType null
        dto.setAccountName("acc");
        dto.setBrokerType(null);
        assertThrows(NullPointerException.class, () -> mapper.toBettingAccountDomain(dto));
    }

    @Test
    void toBettingAccountDomain_mapsFields() {
        var dto = new CreateBettingAccountDto();
        dto.setAccountName("Main");
        dto.setBrokerType(BrokerType.BET365);

        var dom = mapper.toBettingAccountDomain(dto);

        assertEquals("Main", dom.getAccountName());
        assertEquals(BrokerType.BET365, dom.getBrokerType());
    }

    @Test
    void toBettingAccountDto_mapsDraftSlipTransactionsAndBetHistory() {
        var acc = new BettingAccount("A1", BrokerType.BET365);
        acc.setId(10L);
        acc.setBalance(new Money(BigDecimal.valueOf(123.45)));

        // draft slip
        var draft = new DraftBetSlip(BetCategory.SINGLE);
        draft.setId(99L);
        draft.setStake(new Money(BigDecimal.valueOf(5)));
        draft.setPotentialWinning(new Money(new BigDecimal(10)));
        draft.setStatus(BetStatus.CREATING);
        draft.setTotalOdds(2.0);
        draft.setDraftSlipOwner(acc);
        draft.setPicks(new ArrayList<>());
        acc.putEmptySlip(draft);

        // transaction history
        var t1 = new Transaction(new Money(BigDecimal.valueOf(50)), new Money(BigDecimal.valueOf(150)), /*createdAt*/ null, TransactionType.DEPOSIT, "dep");
        t1.setId(1L);
        acc.addTransaction(t1);

        // bet history
        var slip = new BetSlip(BetCategory.SINGLE);
        slip.setId(2L);
        slip.setStake(new Money(BigDecimal.valueOf(20)));
        slip.setPotentialWinning(new Money(BigDecimal.valueOf(40)));
        slip.setStatus(BetStatus.WON);
        slip.setTotalOdds(2.0);
        slip.setPicks(new ArrayList<>());
        acc.addBetSlip(slip);

        BettingAccountDto dto = mapper.toBettingAccountDto(acc);

        assertEquals(10L, dto.getId());
        assertEquals("A1", dto.getAccountName());
        assertEquals(BrokerType.BET365, dto.getBrokerType());
        assertEquals(BigDecimal.valueOf(123.45), dto.getBalance());

        assertNotNull(dto.getDraftAccount());
        assertEquals(99L, dto.getDraftAccount().getId());
        assertEquals(BetStatus.CREATING, dto.getDraftAccount().getStatus());

        assertNotNull(dto.getTransactionHistory());
        assertEquals(1, dto.getTransactionHistory().size());
        assertEquals(1L, dto.getTransactionHistory().get(0).getId());

        assertNotNull(dto.getBetHistory());
        assertEquals(1, dto.getBetHistory().size());
        assertEquals(2L, dto.getBetHistory().get(0).getId());
        assertEquals(BetStatus.WON, dto.getBetHistory().get(0).getStatus());
    }

    @Test
    void toMobileMoneyDomain_requiresNonNull() {
        assertThrows(NullPointerException.class, () -> mapper.toMobileMoneyDomain(1L, null));

        var dto = new CreateMobileMoneyAccountDto();
        dto.accountType = null;
        assertThrows(NullPointerException.class, () -> mapper.toMobileMoneyDomain(1L, dto));
    }

    @Test
    void toMobileMoneyDto_mapsTransactions() {
        var acc = new MobileMoneyAccount(7L, MomoAccountType.ORANGE);
        acc.setAccountBalance(new Money(BigDecimal.valueOf(500)));
        acc.setDailyLimit(true);
        acc.setWeeklyLimit(true);
        acc.setMonthlyLimit(true);

        var t = new Transaction(new Money(BigDecimal.valueOf(10)), new Money(BigDecimal.valueOf(510)), null, TransactionType.DEPOSIT, "topup");
        t.setId(55L);
        acc.addTransaction(t);

        ReadMomoAccountDto dto = mapper.toMobileMoneyDto(acc);

        assertEquals(7L, dto.getId());
        assertEquals(new Money(BigDecimal.valueOf(500)).getValue(), dto.getAccountBalance());
        assertEquals(MomoAccountType.ORANGE, dto.getAccountType());
        assertEquals(true, dto.getDailyLimit());
        assertEquals(1, dto.getTransactionHistory().size());
        assertEquals(55L, dto.getTransactionHistory().get(0).getId());
    }

    @Test
    void toMatchDomain_throwsIfNoOutcomes() {
        var dto = new MatchDto();
        dto.setHome("A");
        dto.setAway("B");
        dto.setMatchOutComes(List.of()); // empty

        assertThrows(IllegalArgumentException.class, () -> mapper.toMatchDomain(dto));
    }

    @Test
    void toBetSlipDomain_mapsCoreFields() {
        var slipDto = new BetSlipDto();
        slipDto.setCategory(BetCategory.SINGLE);
        slipDto.setStake(BigDecimal.valueOf(10));
        slipDto.setTotalOdds(1.9);
        slipDto.setStatus(BetStatus.CREATING);

        var dom = mapper.toBetSlipDomain(slipDto);

        assertEquals(BetCategory.SINGLE, dom.getCategory());
        assertEquals(new Money(BigDecimal.valueOf(10)).getValue(), dom.getStake().getValue());
        assertEquals(1.9, dom.getTotalOdds());
        assertEquals(BetStatus.CREATING, dom.getStatus());
    }

    @Test
    void toReducerDto_mapsMatchesAndSlips() {
        var r = new Reducer(new Money(BigDecimal.valueOf(100)), new Money(BigDecimal.valueOf(5)));
        r.setId(1L);

        var m = new Match("Home", "Away");
        m.setMatchId(10L);
        m.addPick(new MatchEventPick("KEY", "HOME_WIN", 1.5));
        r.setBetMatches(List.of(m));

        var s = new BetSlip(BetCategory.SINGLE);
        s.setId(20L);
        s.setStake(new Money(BigDecimal.valueOf(100)));
        s.setPotentialWinning(new Money(BigDecimal.valueOf(15)));
        s.setStatus(BetStatus.WON);
        s.setPicks(new ArrayList<>());
        r.setSlips(List.of(s));

        ReadReducerDto dto = mapper.toReducerDto(r);

        assertEquals(1L, dto.id());
        assertEquals(new Money(BigDecimal.valueOf(100)).getValue(), dto.totalStake());
        assertEquals(new Money(BigDecimal.valueOf(5)).getValue(), dto.bonusAmount());
        assertEquals(1, dto.betMatchDtos().size());
        assertEquals(1, dto.slips().size());
    }
}