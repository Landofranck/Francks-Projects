package project.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//this class should create a liste of the betslip combinations, it is also stored in the
public class Reducer implements Account {
    private Long id;
    private Money totalStake;
    private List<Match> betMatches;
    private BetSlip newSlip;
    private List<BetSlip> slips;
    private Money bonusAmount;

    public Reducer(Money stake, Money bonusAmount) {
        this.totalStake = Objects.requireNonNull(stake);
        this.bonusAmount = Objects.requireNonNull(bonusAmount);
        this.slips = new ArrayList<>();
        this.betMatches = new ArrayList<>();
    }

    public void addMatches(Match match) {
        this.betMatches.add(Objects.requireNonNull(match));

    }

    public void addEventToSlip(BetSlip slip, MatchEventPick pick) {
        slip.addMatchEventPick(pick);
        slip.makeTotalOdds();
    }

    public BetSlip createBetSlip(String category) {
        Objects.requireNonNull(category);
        BetSlip newBetslip = new BetSlip(category);
        addBetSlip(newBetslip);
        return newBetslip;
    }

    public void addBetSlip(BetSlip b) {
        this.slips.add(Objects.requireNonNull(b));
    }


    /**
     * Build all BetSlip combinations by taking exactly ONE pick from EACH match.
     * Example: [[a,b],[c,d]] -> [a,c], [a,d], [b,c], [b,d]
     */
    public List<BetSlip> createSlips(String category) {
        Objects.requireNonNull(category, "category");

        slips.clear();

        if (betMatches.isEmpty()) {
            return slips; // nothing to combine
        }

        // Start with one empty slip (neutral element for cartesian product)
        List<BetSlip> current = new ArrayList<>();
        current.add(new BetSlip(category));

        for (Match match : betMatches) {
            Objects.requireNonNull(match, "match in betMatches");

            List<MatchEventPick> outcomes = match.getMatchOutComes();
            if (outcomes == null || outcomes.isEmpty()) {
                // If a match has no outcomes, there can be no valid full combinations
                current.clear();
                break;
            }

            List<BetSlip> next = new ArrayList<>();

            for (BetSlip partialSlip : current) {
                for (MatchEventPick pick : outcomes) {
                    // IMPORTANT: copy pick so owner isn't overwritten across slips
                    MatchEventPick pickCopy = copyPick(pick);

                    BetSlip newSlip = copySlip(partialSlip, category);
                    newSlip.addMatchEventPick(pickCopy); // sets owner + recomputes totalOdds
                    next.add(newSlip);
                }
            }

            current = next;
        }

        slips.addAll(current);

        // OPTIONAL: distribute stake equally across all slips, if your Money supports division.
        // Example idea (depends on your Money API):
        // if (!slips.isEmpty()) {
        //     Money perSlipStake = totalStake.divide(slips.size());
        //     for (BetSlip s : slips) s.setStake(perSlipStake);
        // }

        return slips;
    }

    // ---- helpers ----

    private MatchEventPick copyPick(MatchEventPick original) {
        Objects.requireNonNull(original, "original pick");
        MatchEventPick copy = new MatchEventPick(original.getMatchKey(),original.getOutcomeName(), original.getOdd());
        copy.setMatchKey(original.getMatchKey());
        return copy;
    }

    /**
     * Copies a slip's picks into a new slip instance (deep copy of picks).
     */
    private BetSlip copySlip(BetSlip original, String category) {
        BetSlip copy = new BetSlip(category);

        for (MatchEventPick p : original.getPicks()) {
            copy.addMatchEventPick(copyPick(p));
        }

        // copy.setCreatedAt(original.getCreatedAt()); // if you want
        // copy.setStake(original.getStake());         // if you want

        return copy;
    }

    public void setTheSlipStakes() {
        if (slips.isEmpty()) return;

        for (BetSlip s : slips) {
            double odds = s.getTotalOdds();
            if (odds <= 0) throw new IllegalStateException("totalOdds must be > 0");
            s.setStake(totalStake.divide(odds));
        }
    }


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
