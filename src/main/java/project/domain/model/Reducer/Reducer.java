package project.domain.model.Reducer;

import project.domain.model.*;
import project.domain.model.Enums.BetCategory;
import project.domain.model.Enums.BlockType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Reducer generates BetSlip combinations using a Cascade-Expanded Outcome Cover (CEOC).
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
    private List<Match> betMatches;
    private List<BetSlip> slips;
    private Money bonusAmount;

    // Block configuration (optional; if null/empty -> FULL over all matches)
    private List<Block> blocks;

    public Reducer(Money stake, Money bonusAmount) {
        this.totalStake = Objects.requireNonNull(stake);
        this.bonusAmount = Objects.requireNonNull(bonusAmount);
        this.slips = new ArrayList<>();
        this.betMatches = new ArrayList<>();
        this.blocks = new ArrayList<>();
    }

    public void addMatches(Match match) {
        this.betMatches.add(Objects.requireNonNull(match));
    }

    public BetSlip createBetSlip(BetCategory category) {
        Objects.requireNonNull(category);
        BetSlip newBetslip = new BetSlip(category);
        addBetSlip(newBetslip);
        return newBetslip;
    }

    public void addBetSlip(BetSlip b) {
        this.slips.add(Objects.requireNonNull(b));
    }

    /* -------------------- Block API -------------------- */




    public void setBlocks(List<Block> blocks) {
        this.blocks = Objects.requireNonNull(blocks, "blocks");
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    /* -------------------- NEW createSlips (replaces old cartesian only) -------------------- */
    /**
     * Generates bet slips according to the configured block structure.
     *
     * <p>
     * If no blocks are configured, the reducer defaults to a FULL block
     * spanning all matches (classic cartesian generation).
     * </p>
     *
     * @param category bet category for generated slips
     * @return list of generated BetSlip instances
     */
    public List<BetSlip> createSlips(BetCategory category) {
        Objects.requireNonNull(category, "category");

        slips.clear();
        if (betMatches == null || betMatches.isEmpty()) return slips;

        // fallback = FULL across all matches
        if (blocks == null || blocks.isEmpty()) {
            blocks = List.of(new Block(BlockType.FULL, 0, betMatches.size() - 1));
        }

        validateBlocks(blocks);

        List<BetSlip> out = new ArrayList<>();
        backtrackBlocks(category, 0, new ArrayList<>(), out);

        slips.addAll(out);
        setTheSlipStakes();
        return slips;
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
            List<MatchEventPick> prefixPicks,
            List<BetSlip> out
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
            List<MatchEventPick> prefixPicks,
            List<BetSlip> out,
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
        List<MatchEventPick> outcomes = requireOutcomes(match, matchIdx);

        for (MatchEventPick pick : outcomes) {
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
            List<MatchEventPick> prefixPicks,
            List<BetSlip> out,
            boolean hasNext,
            int blockIndex
    ) {
        if (matchIdx > block.getEndMatchIdx()) {
            if (hasNext) backtrackBlocks(category, blockIndex + 1, prefixPicks, out);
            else out.add(buildSlipFrom(prefixPicks, category));
            return;
        }

        Match match = betMatches.get(matchIdx);
        List<MatchEventPick> outcomes = requireOutcomes(match, matchIdx);

        MatchEventPick fPick = pickByCascadeOutcome(outcomes, CascadeOutcome.F);
        MatchEventPick ePick = pickByCascadeOutcome(outcomes, CascadeOutcome.E);
        MatchEventPick dPick = pickByCascadeOutcome(outcomes, CascadeOutcome.D);

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

    private enum CascadeOutcome { F, E, D }

    private MatchEventPick pickByCascadeOutcome(List<MatchEventPick> outcomes, CascadeOutcome which) {
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

    private boolean isDominantPick(MatchEventPick pick, Match match) {
        List<MatchEventPick> outcomes = match.getMatchOutComes();
        if (outcomes == null || outcomes.isEmpty()) return false;

        MatchEventPick dominant = outcomes.get(outcomes.size() - 1); // dominant = last
        return Objects.equals(pick.getOutcomeName(), dominant.getOutcomeName())
                && Double.compare(pick.getOdd(), dominant.getOdd()) == 0
                && Objects.equals(pick.getMatchKey(), dominant.getMatchKey());
    }

    private List<MatchEventPick> requireOutcomes(Match match, int matchIdx) {
        Objects.requireNonNull(match, "match at index " + matchIdx);
        List<MatchEventPick> outcomes = match.getMatchOutComes();
        if (outcomes == null || outcomes.isEmpty()) {
            throw new IllegalStateException("Match at index " + matchIdx + " has no outcomes");
        }
        return outcomes;
    }

    /* -------------------- Slip building / copying -------------------- */

    private BetSlip buildSlipFrom(List<MatchEventPick> picks, BetCategory category) {
        BetSlip s = new BetSlip(category);
        for (MatchEventPick p : picks) {
            s.addMatchEventPick(copyPick(p));
        }
        s.makeTotalOdds();
        return s;
    }

    private MatchEventPick copyPick(MatchEventPick original) {
        Objects.requireNonNull(original, "original pick");
        MatchEventPick copy = new MatchEventPick(original.getMatchKey(), original.getOutcomeName(), original.getOdd());
        copy.setMatchKey(original.getMatchKey());
        return copy;
    }

    /* -------------------- Stakes (unchanged) -------------------- */

    public void setTheSlipStakes() {
        if (slips.isEmpty()) return;

        for (BetSlip s : slips) {
            double odds = s.getTotalOdds();
            if (odds <= 0) throw new IllegalStateException("totalOdds must be > 0");
            s.setStake(totalStake.divide(odds));
            s.calculatPotentialWinning();
        }
    }

    /* -------------------- Account + getters/setters (unchanged) -------------------- */

    @Override
    public Long getAccountId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BetSlip> getSlips() {
        return this.slips;
    }

    public List<Match> getBetMatches() {
        return betMatches;
    }

    public Money getTotalStake() {
        return totalStake;
    }

    public Money getBonusAmount() {
        return bonusAmount;
    }

    public void setBetMatches(List<Match> betMatches) {
        this.betMatches = betMatches;
    }

    public void setBonusAmount(Money bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public void setSlips(List<BetSlip> slips) {
        this.slips = slips;
    }

    public void setTotalStake(Money totalStake) {
        this.totalStake = totalStake;
    }

}