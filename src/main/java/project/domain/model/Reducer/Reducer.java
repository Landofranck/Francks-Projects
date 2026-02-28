package project.domain.model.Reducer;


import project.domain.model.*;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BetStrategy;
import project.domain.model.Enums.BlockType;
import project.domain.model.Enums.BrokerType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Reducer generates ReducerBetSlip combinations using a Cascade-Expanded Outcome Cover (CEOC).
 *
 * <p>
 * Instead of generating the full cartesian product of match outcomes,
 * the reducer expands matches left-to-right using configurable blocks:
 * </p>
 *
 * <ul>
 *   <li><b>FULL blocks</b> enumerate all outcomes for each match in the block.</li>
 *   <li><b>CASCADE blocks</b> expand sequentially and terminate early unless the
 *       dominant outcome is encountered.</li>
 * </ul>
 *
 * <p>
 * For CASCADE blocks, each match has three ordered outcomes:
 * </p>
 * <ul>
 *   <li>F — Favorable (terminates the slip)</li>
 *   <li>E — Neutral (terminates the slip)</li>
 *   <li>D — Dominant (continues expansion)</li>
 * </ul>
 *
 * <p>
 * Expansion proceeds to the next block <em>only if</em> all matches in the current
 * block evaluate to the dominant outcome.
 * </p>
 *
 * <p>
 * This strategy guarantees full coverage of the outcome space while producing
 * significantly fewer slips than naive cartesian enumeration.
 * </p>
 */

public class Reducer implements Account {
    private Long id;
    private Money totalStake;
    private Long matchVersion;
    private List<Match> betMatches;
    private List<ReducerBetSlip> slips;
    private Money totalStaked;
    private BigDecimal profitOrLoss;
    //determines which type of matches are to be added here
    private BrokerType broker;

    private Money bonusAmount;

    // Block configuration (optional; if null/empty -> FULL over all matches)

    private List<Block> blocks;
    private List<Shuffle> shuffleCombinations;
    private BetStrategy strategy;

    public Reducer(Money stake, Money bonusAmount, BetStrategy strategy, BrokerType broker) {
        this.totalStake = Objects.requireNonNull(stake);
        this.bonusAmount = Objects.requireNonNull(bonusAmount);
        this.slips = new ArrayList<>();
        this.betMatches = new ArrayList<>();
        this.blocks = new ArrayList<>();
        this.matchVersion = -1L;
        this.strategy = strategy;
        this.broker = broker;
        this.profitOrLoss = BigDecimal.ZERO;
        this.totalStaked = new Money(0);
        this.shuffleCombinations = new ArrayList<>();
    }

    public Long updateMatchVersion() {
        this.matchVersion = -1L;
        for (Match m : betMatches) {
            this.matchVersion += m.getVersion();
        }
        return this.matchVersion;
    }


    /* -------------------- Block API -------------------- */


    public void setBlocks(List<Block> blocks) {
        this.blocks = Objects.requireNonNull(blocks, "blocks");
    }

    /* -------------------- NEW computeSlips (replaces old Cartesian only) -------------------- */

    /**
     * Generates bet slips according to the configured block structure.
     *
     * <p>
     * If no blocks are configured, the reducer defaults to a FULL block
     * spanning all matches (classic cartesian generation).
     * </p>
     *
     * @param category bet category for generated slips
     */
    public void computeSlips(BetCategory category) {
        Objects.requireNonNull(category, "category");

        slips.clear();
        if (betMatches == null || betMatches.isEmpty()) return;

        // fallback = FULL across all matches
        if (blocks == null || blocks.isEmpty()) {
            blocks = List.of(new Block(BlockType.FULL, 0, betMatches.size() - 1));
        }

        validateBlocks(blocks);

        List<ReducerBetSlip> out = new ArrayList<>();
        backtrackBlocks(category, 0, new ArrayList<>(), out);

        slips.addAll(out);
        setTheSlipStakes();
        checkProfitvalues();
        checkSchuffle();
    }

    private void validateBlocks(List<Block> blocks) {
        int n = betMatches.size();
        for (Block b : blocks) {
            if (b.getEndMatchIdx() >= n) {
                throw new IllegalStateException("Block exceeds matches size: " + b.getStartMatchIdx() + ".." + b.getEndMatchIdx()
                        + " (matches size=" + n + ")");
            }
        }
        // Optional: you can enforce contiguity / non-overlap here if you want.
    }

    private void backtrackBlocks(
            BetCategory category,
            int blockIndex,
            List<MatchOutComePick> prefixPicks,
            List<ReducerBetSlip> out
    ) {
        if (blockIndex >= blocks.size()) {
            if (!prefixPicks.isEmpty()) out.add(buildSlipFrom(prefixPicks, category));
            return;
        }

        Block b = blocks.get(blockIndex);
        boolean hasNext = blockIndex + 1 < blocks.size();

        if (b.getType() == BlockType.FULL) {
            fullEnumerate(category, b, b.getStartMatchIdx(), prefixPicks, out, hasNext, blockIndex);
        } else {
            cascadeEnumerate(category, b, b.getStartMatchIdx(), prefixPicks, out, hasNext, blockIndex);
        }
    }

    private void fullEnumerate(
            BetCategory category,
            Block block,
            int matchIdx,
            List<MatchOutComePick> prefixPicks,
            List<ReducerBetSlip> out,
            boolean hasNext,
            int blockIndex
    ) {
        if (matchIdx > block.getEndMatchIdx()) {
            boolean allDominant = true;

            int len = block.length();
            int start = prefixPicks.size() - len;

            for (int i = start; i < prefixPicks.size(); i++) {
                int local = i - start; // 0..len-1
                Match match = betMatches.get(block.getStartMatchIdx() + local);
                if (!isDominantPick(prefixPicks.get(i), match)) {
                    allDominant = false;
                    break;
                }
            }

            if (hasNext && allDominant) {
                backtrackBlocks(category, blockIndex + 1, prefixPicks, out);
            } else {
                out.add(buildSlipFrom(prefixPicks, category));
            }
            return;
        }

        Match match = betMatches.get(matchIdx);
        List<MatchOutComePick> outcomes = requireOutcomes(match, matchIdx);

        for (MatchOutComePick pick : outcomes) {
            prefixPicks.add(copyPick(pick));
            fullEnumerate(category, block, matchIdx + 1, prefixPicks, out, hasNext, blockIndex);
            prefixPicks.remove(prefixPicks.size() - 1);
        }
    }

    /**
     * Performs cascade expansion:
     * - F/E outcomes terminate the slip immediately
     * - D outcomes continue expansion
     */
    private void cascadeEnumerate(
            BetCategory category,
            Block block,
            int matchIdx,
            List<MatchOutComePick> prefixPicks,
            List<ReducerBetSlip> out,
            boolean hasNext,
            int blockIndex
    ) {
        if (matchIdx > block.getEndMatchIdx()) {
            if (hasNext) backtrackBlocks(category, blockIndex + 1, prefixPicks, out);
            else out.add(buildSlipFrom(prefixPicks, category));
            return;
        }

        Match match = betMatches.get(matchIdx);
        List<MatchOutComePick> outcomes = requireOutcomes(match, matchIdx);

        MatchOutComePick fPick = pickByCascadeOutcome(outcomes, CascadeOutcome.F);
        MatchOutComePick ePick = pickByCascadeOutcome(outcomes, CascadeOutcome.E);
        MatchOutComePick dPick = pickByCascadeOutcome(outcomes, CascadeOutcome.D);

        // F terminates
        prefixPicks.add(fPick);
        out.add(buildSlipFrom(prefixPicks, category));
        prefixPicks.remove(prefixPicks.size() - 1);

        // E terminates
        prefixPicks.add(ePick);
        out.add(buildSlipFrom(prefixPicks, category));
        prefixPicks.remove(prefixPicks.size() - 1);

        // D continues
        prefixPicks.add(dPick);
        cascadeEnumerate(category, block, matchIdx + 1, prefixPicks, out, hasNext, blockIndex);
        prefixPicks.remove(prefixPicks.size() - 1);
    }


    /* -------------------- Outcome mapping (CASCADE) -------------------- */


    private enum CascadeOutcome {F, E, D;}

    private MatchOutComePick pickByCascadeOutcome(List<MatchOutComePick> outcomes, CascadeOutcome which) {
        if (outcomes.size() < 3) {
            throw new IllegalStateException("CASCADE requires >= 3 outcomes, got " + outcomes.size());
        }
        int idx = switch (which) {
            case F -> 0;
            case E -> 1;
            case D -> outcomes.size() - 1; // dominant
        };
        return copyPick(outcomes.get(idx));
    }

    private boolean isDominantPick(MatchOutComePick pick, Match match) {
        List<MatchOutComePick> outcomes = match.getMatchOutComes();
        if (outcomes == null || outcomes.isEmpty()) return false;

        MatchOutComePick dominant = outcomes.get(outcomes.size() - 1); // dominant = last
        return Objects.equals(pick.getOutcomeName(), dominant.getOutcomeName())
                && Double.compare(pick.getOdd(), dominant.getOdd()) == 0
                && Objects.equals(pick.getMatchKey(), dominant.getMatchKey());
    }

    private List<MatchOutComePick> requireOutcomes(Match match, int matchIdx) {
        Objects.requireNonNull(match, "match at index " + matchIdx);
        List<MatchOutComePick> outcomes = match.getMatchOutComes();
        if (outcomes == null || outcomes.isEmpty()) {
            throw new IllegalStateException("Match at index " + matchIdx + " has no outcomes");
        }
        return outcomes;
    }


    /* -------------------- Slip building / copying -------------------- */
    private ReducerBetSlip buildSlipFrom(List<MatchOutComePick> picks, BetCategory category) {
        ReducerBetSlip s = new ReducerBetSlip(category, this.strategy, this.broker);
        for (MatchOutComePick p : picks) {
            s.addMatchEventPick(copyPick(p));
        }
        s.makeTotalOdds();
        s.updateCategory();
        return s;
    }

    private MatchOutComePick copyPick(MatchOutComePick original) {
        Objects.requireNonNull(original, "original pick");
        MatchOutComePick copy = new MatchOutComePick(original.getIdentity(), original.getMatchKey(), original.getOutcomeName(), original.getOdd(), original.getLeague());
        copy.setMatchKey(original.getMatchKey());
        copy.setOwnerMatchName(original.getOwnerMatchName());
        copy.setBegins(original.getBegins());
        copy.setOutcomePickStatus(original.getOutcomePickStatus());
        return copy;
    }


    /* -------------------- Stakes (unchanged) -------------------- */
    public void setTheSlipStakes() {
        if (slips.isEmpty()) return;

        for (ReducerBetSlip s : slips) {
            double odds = s.getTotalOdds();
            if (odds <= 0) throw new IllegalStateException("totalOdds must be > 0");
            s.setPlanedStake(totalStake.divide(odds));
            s.setRemainingStake(totalStake.divide(odds));
            s.calculatePotentialWinning();
        }
    }

    /* -------------------- Profits (unchanged) -------------------- */
    private void checkProfitvalues() {
        this.totalStaked = new Money(BigDecimal.ZERO);
        for (ReducerBetSlip s : slips) {
            this.totalStaked = totalStaked.add(s.getPlanedStake());
        }
        profitOrLoss = totalStake.getValue().subtract(totalStaked.getValue());
    }

    public void checkSchuffle() {
        shuffleCombinations.clear();
        var ids = new ArrayList<Long>();
        for (Match match : betMatches) {
            ids.add(match.getMatchId());
        }

        shuffle(ids, new ArrayList<>(), new boolean[ids.size()], this.shuffleCombinations);
    }

    private static void shuffle(List<Long> ids,
                                List<Long> current,
                                boolean[] used,
                                List<Shuffle> result) {

        if (current.size() == ids.size()) {
            result.add(new Shuffle(new ArrayList<>(current)));
            return;
        }

        for (int i = 0; i < ids.size(); i++) {
            if (used[i]) continue;

            used[i] = true;
            current.add(ids.get(i));

            shuffle(ids, current, used, result);
            current.remove(current.size() - 1);
            used[i] = false;
        }

    }

    /* -------------------- Account + getters/setters (unchanged) -------------------- */
    @Override
    public Long getAccountId() {
        return this.id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Money getTotalStake() {
        return totalStake;
    }

    public void setTotalStake(Money totalStake) {
        this.totalStake = totalStake;
    }

    public List<Match> getBetMatches() {
        return betMatches;
    }

    public void setBetMatches(List<Match> betMatches) {
        this.betMatches = betMatches;
    }

    public List<ReducerBetSlip> getSlips() {
        return slips;
    }

    public void setSlips(List<ReducerBetSlip> slips) {
        this.slips = slips;
    }

    public Money getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Money bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public Long getMatchVersion() {
        return matchVersion;
    }

    public void setMatchVersion(Long matchVersion) {
        this.matchVersion = matchVersion;
    }

    public BetStrategy getStrategy() {
        return this.strategy;
    }

    public void setStrategy(BetStrategy strategy) {
        this.strategy = strategy;
    }

    public BrokerType getBroker() {
        return broker;
    }

    public void setBroker(BrokerType broker) {
        this.broker = broker;
    }

    public Money getTotalStaked() {
        return totalStaked;
    }

    public void setTotalStaked(Money totalStaked) {
        this.totalStaked = totalStaked;
    }

    public BigDecimal getProfitOrLoss() {
        return profitOrLoss;
    }

    public void setProfitOrLoss(BigDecimal profitOrLoss) {
        this.profitOrLoss = profitOrLoss;
    }

    public void setShuffleCombinations(List<Shuffle> shuffleCombinations) {
        this.shuffleCombinations = new ArrayList<>(
                Objects.requireNonNull(shuffleCombinations)
        );
    }

    public List<Shuffle> getShuffleCombinations() {
        return shuffleCombinations;
    }
}