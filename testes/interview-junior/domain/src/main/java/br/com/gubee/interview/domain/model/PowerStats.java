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
    private int strength;
    private int agility;
    private int dexterity;
    private int intelligence;
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

    public void update(Integer newStrength, Integer newAgility, Integer newDexterity, Integer newIntelligence) {

        if (newStrength != null && newStrength >= 0 && newStrength <= 10) {
            this.strength = newStrength;
        }

        if (newAgility != null && newAgility >= 0 && newAgility <= 10) {
            this.agility = newAgility;
        }

        if (newDexterity != null && newDexterity >= 0 && newDexterity <= 10) {
            this.dexterity = newDexterity;
        }

        if (newIntelligence != null && newIntelligence >= 0 && newIntelligence <= 10) {
            this.intelligence = newIntelligence;
        }

        this.updatedAt = Instant.now();

    }

    public Integer getTotal() {
        return strength + agility + dexterity + intelligence;
    }
}
