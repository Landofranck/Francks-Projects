package UnitTest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import project.adapter.out.persistence.Embeddables.BlockEmb;
import project.adapter.out.persistence.EntityModels.*;
import project.adapter.out.persistence.EntityModels.BettingAccount.BetSlipEntity;
import project.adapter.out.persistence.EntityModels.BettingAccount.MatchEntity;
import project.adapter.out.persistence.Mappers.BettingAccountMapper;
import project.adapter.out.persistence.Mappers.ReducerMapper;
import project.domain.model.*;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.BlockType;
import project.domain.model.Reducer.Block;
import project.domain.model.Reducer.Reducer;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ReducerMapperTest {

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
        var Block=new Block(BlockType.FULL,0,0);
        r.setBlocks(List.of(Block));

        ReducerEntity e = mapper.toReducerEntity(r);

        assertEquals(null, e.getId());
        assertEquals(new Money(100).getValue(), e.getTotalStake());
        assertEquals(new Money(5).getValue(), e.getBonusAmount());
        assertEquals(1, e.getSlips().size());
        assertEquals(1, e.getBetMatcheEntities().size());
        assertEquals(1, e.getBlockEmbs().size());
        assertEquals(BlockType.FULL,e.getBlockEmbs().get(0).getType());
    }

    @Test
    void toReducerDomain_mapsSlipsAndMatches() {
        var e = new ReducerEntity();
        e.setId(1L);
        e.setTotalStake(new Money(100).getValue());
        e.setBonusAmount(new Money(5).getValue());

        var slipE = new BetSlipEntity();
        slipE.setCategory(BetCategory.SINGLE);
        slipE.setStake(new Money(10).getValue());
        slipE.setPotentialWinning(new Money(20).getValue());
        slipE.setStatus(BetStatus.WON);
        slipE.setTotalOdd(1);
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
        e.addMatches(matchE);

        var blockEmb=new BlockEmb();
        blockEmb.setEndMatchIdx(0);
        blockEmb.setStartMatchIdx(0);
        blockEmb.setType(BlockType.FULL);
        e.setBlockEmbs(List.of(blockEmb));

        Reducer dom = mapper.toReducerDomain(e);

        assertEquals(1L, dom.getAccountId());
        assertEquals(new Money(100).getValue(), dom.getTotalStake().getValue());
        assertEquals(new Money(5).getValue(), dom.getBonusAmount().getValue());
        assertNotNull(dom.getSlips());
        assertEquals(1, dom.getSlips().size());
        assertNotNull(dom.getBetMatches());
        assertEquals(BlockType.FULL,dom.getBlocks().get(0).getType());
        assertEquals(1, dom.getBlocks().size());
        assertNotNull(dom.getBlocks());

        assertEquals(1, dom.getBetMatches().size());
    }
}
