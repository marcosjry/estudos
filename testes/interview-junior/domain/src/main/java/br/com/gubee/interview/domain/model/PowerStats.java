package br.com.gubee.interview.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PowerStats {

    private UUID id;
    private Integer strength;
    private Integer agility;
    private Integer dexterity;
    private Integer intelligence;
    private Instant createdAt;
    private Instant updatedAt;

    public PowerStats() {

    }

    public PowerStats(UUID id, Integer strength, Integer agility, Integer dexterity, Integer intelligence) {
        this.id = id;
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    public PowerStats( Integer strength, Integer agility, Integer dexterity, Integer intelligence) {

        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;

    }

    public void update(PowerStats powerStatsToUpdate) {
        if(powerStatsToUpdate == null) {
            return;
        }

        if (powerStatsToUpdate.getStrength() != null && powerStatsToUpdate.getStrength() >= 0 && powerStatsToUpdate.getStrength() <= 10) {
            this.strength = powerStatsToUpdate.getStrength();
        }

        if (powerStatsToUpdate.getAgility() != null && powerStatsToUpdate.getAgility() >= 0 && powerStatsToUpdate.getAgility() <= 10) {
            this.agility = powerStatsToUpdate.getAgility();
        }

        if (powerStatsToUpdate.getDexterity() != null && powerStatsToUpdate.getDexterity() >= 0 && powerStatsToUpdate.getDexterity() <= 10) {
            this.dexterity = powerStatsToUpdate.getDexterity();
        }

        if (powerStatsToUpdate.getIntelligence() != null && powerStatsToUpdate.getIntelligence() >= 0 && powerStatsToUpdate.getIntelligence() <= 10) {
            this.intelligence = powerStatsToUpdate.getIntelligence();
        }

        this.updatedAt = Instant.now();

    }

    public Integer getTotal() {
        return strength + agility + dexterity + intelligence;
    }
}
