package project.adapter.out.persistence.EntityModels;

import jakarta.persistence.*;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import project.domain.model.BettingAccount;
import project.domain.model.Enums.BetStatus;
import project.domain.model.MatchEventPick;
import project.domain.model.Money;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BetSlipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "parentBetSlipEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchEventPickEntity> picks;
    private BetStatus status;
    private String category;
    private Instant createdAt;
    private double totalOdd;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "bettingAccountEntity_id", nullable = false)
    private AccountEntity parentAccountEntity;

    @Embedded
    private Money stake;

    protected BetSlipEntity() {
    }

    public BetSlipEntity(String category, Instant createdAt) {
        this.picks = new ArrayList<>();
        this.category = category;
        this.createdAt = createdAt;
    }


    public void setTotalOdd(double totalOdd) {
        this.totalOdd = totalOdd;
    }

    public double getTotalOdd() {
        return totalOdd;
    }

    public void addMatchEventPickEntity(MatchEventPickEntity entity){
        this.picks.add(entity);
        entity.setParent(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BetStatus getStatus() {
        return status;
    }

    public void setStatus(BetStatus status) {
        this.status = status;
    }

    public List<MatchEventPickEntity> getPicks() {
        return picks;
    }

    public String getCategory() {
        return category;
    }

    public void setPicks(List<MatchEventPickEntity> picks) {
        this.picks = picks;
    }

    public void setOwner(AccountEntity bettingAccount) {
        this.parentAccountEntity = bettingAccount;
    }

    public AccountEntity getParentAccount() {
        return parentAccountEntity;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Money getStake() {
        return stake;
    }


    public void setCategory(String category) {
        this.category = category;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setParentAccount(AccountEntity parentAccount) {
        this.parentAccountEntity = parentAccount;
    }

    public void setStake(Money stake) {
        this.stake = stake;
    }

}
