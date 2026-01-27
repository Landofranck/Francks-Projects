package UnitTest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.adapter.out.persistence.BettingAccountRepositoryJpa;
import project.adapter.out.persistence.EntityModels.*;
import project.adapter.out.persistence.Mappers.BettingAccountMapper;
import project.adapter.out.persistence.Mappers.ReducerMapper;
import project.application.port.out.Match.ReadMatchByIdPort;
import project.application.service.BettingAccounts.GetMatchByIdUseCaseImpl;
import project.domain.model.*;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BetStatus;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReducerMapperTest {

    @Mock
    GetMatchByIdUseCaseImpl getMatch;

    @InjectMocks
    BettingAccountRepositoryJpa jpa;
    private BettingAccountMapper betMapper;
    private ReducerMapper mapper;

    @BeforeEach
    void setUp() throws Exception {
        mapper = new ReducerMapper();

        // Inject BettingAccountMapper manually (because in production CDI does it)
        var betMapper = new BettingAccountMapper();
        Field f = ReducerMapper.class.getDeclaredField("betMapper");
        f.setAccessible(true);
        f.set(mapper, betMapper);
    }

    @Test
    void toReducerEntity_mapsSlipsAndMatches() {
        var r = new Reducer(new Money(BigDecimal.valueOf(100)), new Money(5));
        r.setId(1L);

        var slip = new BetSlip(BetCategory.SINGLE);
        slip.setStake(new Money(BigDecimal.valueOf(10)));
        slip.setPotentialWinning(new Money(20));
        slip.setStatus(BetStatus.WON);
        slip.setPicks(new ArrayList<>());
        r.setSlips(new ArrayList<>());
        r.getSlips().add(slip);

        var match = new Match("H", "A");
        match.addPick(new MatchEventPick("K", "HOME", 1.5));
        r.setBetMatches(new ArrayList<>());
        r.getBetMatches().add(match);

        ReducerEntity e = mapper.toReducerEntity(r);

        assertEquals(1L, e.getId());
        assertEquals(new Money(100).getValue(), e.getTotalStake());
        assertEquals(new Money(5).getValue(), e.getBonusAmount());
        assertEquals(1, e.getSlips().size());
        assertEquals(1, e.getBetMatches().size());
    }

    @Test
    void toReducerDomain_mapsSlipsAndMatches() {
        betMapper=new BettingAccountMapper();
        jpa=new BettingAccountRepositoryJpa();
        mapper=new ReducerMapper();
        var e = new ReducerEntity();
        e.setId(1L);
        e.setTotalStake(new Money(100).getValue());
        e.setBonusAmount(new Money(5).getValue());

        var slipE = new BetSlipEntity();
        slipE.setCategory(BetCategory.SINGLE);
        slipE.setStake(new Money(10).getValue());
        slipE.setPotentialWinning(new Money(20).getValue());
        slipE.setStatus(BetStatus.WON);
        slipE.setTotalOdd(3);
        e.addBetSlipEntity(slipE);

        var matchE = new MatchEntity();
        matchE.setHome("H");
        matchE.setAway("A");
        // need at least one outcome or mapper may throw
        var out = new MatchOutcomeEntity();
        out.setMatchKey("K");
        out.setOutcomeName("HOME");
        out.setOdd(1.5);
        matchE.addOutcome(out);
        Long id = 1L;
        e.addMatches(id);
        var match = betMapper.toMatchDomain(matchE);
        match.setMatchId(1L);
        when(getMatch.loadMatch(id)).thenReturn(match);
        Reducer dom = mapper.toReducerDomain(e);

        assertEquals(1L, dom.getAccountId());
        assertEquals(100, dom.getTotalStake().getValue());
        assertEquals(5, dom.getBonusAmount().getValue());
        assertNotNull(dom.getSlips());
        assertEquals(1, dom.getSlips().size());
        assertNotNull(dom.getBetMatches());
        assertEquals(1, dom.getBetMatches().size());
    }
}
