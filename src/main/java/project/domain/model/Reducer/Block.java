package project.domain.model.Reducer;

import project.domain.model.Enums.BlockType;

import java.util.Objects;

/**
 * Block is defined over match indices (0-based, inclusive).
 * Example: new Block(CASCADE, 0, 0), new Block(FULL, 1, 3), new Block(CASCADE, 4, 8)
 * Defines a contiguous block of matches to be expanded using a specific strategy.
 *
 * <p>
 * Blocks are processed left-to-right. Expansion continues to the next block
 * only if the current block completes with dominant outcomes.
 * </p>
 */
public final class Block {
    private final BlockType type;
    private final int startMatchIdx; // inclusive, 0-based
    private final int endMatchIdx;   // inclusive, 0-based

    public Block(BlockType type, int startMatchIdx, int endMatchIdx) {
        this.type = Objects.requireNonNull(type, "type");
        if (startMatchIdx < 0 || endMatchIdx < startMatchIdx) {
            throw new IllegalArgumentException("Bad block range: " + startMatchIdx + ".." + endMatchIdx);
        }
        this.startMatchIdx = startMatchIdx;
        this.endMatchIdx = endMatchIdx;
    }

    int length() { return endMatchIdx - startMatchIdx + 1; }

    public BlockType getType() {
        return type;
    }

    public int getEndMatchIdx() {
        return endMatchIdx;
    }

    public int getStartMatchIdx() {
        return startMatchIdx;
    }
}