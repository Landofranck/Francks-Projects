package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.Embeddable;

import java.util.ArrayList;
import java.util.List;

@Embeddable
public class ShuffleEmb {
    private List<Long> shuffle = new ArrayList<>();

    public ShuffleEmb(List<Long> shuffle) {
        this.shuffle = shuffle;
    }

    public ShuffleEmb() {
    }

    public List<Long> getShuffle() {
        return shuffle;
    }

    public void setShuffle(List<Long> shuffle) {
        this.shuffle = shuffle;
    }
}
