package project.adapter.out.persistence.Embeddables;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import project.domain.model.Enums.BlockType;

@Embeddable
public class BlockEmb {
    @Enumerated
    private BlockType type;
    private int startMatchIdx; // inclusive, 0-based
    private int endMatchIdx;

    public void setEndMatchIdx(int endMatchIdx) {
        this.endMatchIdx = endMatchIdx;
    }

    public void setStartMatchIdx(int startMatchIdx) {
        this.startMatchIdx = startMatchIdx;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public int getEndMatchIdx() {
        return endMatchIdx;
    }

    public BlockType getType() {
        return type;
    }

    public int getStartMatchIdx() {
        return startMatchIdx;
    }
}
