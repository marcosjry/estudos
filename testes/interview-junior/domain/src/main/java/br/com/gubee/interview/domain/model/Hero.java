package br.com.gubee.interview.domain.model;

import br.com.gubee.interview.domain.enums.Race;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class Hero {

    private UUID id;
    private String name;
    private Race race;
    private PowerStats powerStats;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean enabled;

    public Hero(String name, Race race, PowerStats powerStats) {
        this.name = name;
        this.race = race;
        this.powerStats = powerStats;
        this.enabled = true;
    }

    public Hero(UUID id, String name, Race race, PowerStats powerStats, Instant createdAt, Instant updatedAt, boolean enabled) {
        this.id = id;
        this.name = name;
        this.race = race;
        this.powerStats = powerStats;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.enabled = enabled;
    }

    public Hero(UUID id, String name, Race race, PowerStats powerStats) {
        this.id = id;
        this.name = name;
        this.race = race;
        this.powerStats = powerStats;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.enabled = true;
    }

    public void update(String newName, Race newRace, PowerStats newStats) {
        if (!this.enabled) {
            throw new IllegalStateException("Cannot update a disabled hero.");
        }

        verifyAttributesToUpdate(newName, newRace, newStats);

    }

    private void verifyAttributesToUpdate(String newName, Race newRace, PowerStats newStats) {
        if(newName != null && !newName.isEmpty())
            this.name = newName;

        if(newRace != null && !newRace.name().isEmpty())
            this.race = newRace;

        this.powerStats.update(newStats);

        this.touch();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    public Comparison compareStatsWith(Hero other) {
        return new Comparison(
                this.getPowerStats().getAgility() - other.getPowerStats().getAgility(),
                this.getPowerStats().getDexterity() - other.getPowerStats().getDexterity(),
                this.getPowerStats().getStrength() - other.getPowerStats().getStrength(),
                this.getPowerStats().getIntelligence() - other.getPowerStats().getIntelligence()
        );
    }
}
