package br.com.gubee.interview.domain;

import br.com.gubee.interview.domain.model.PowerStats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PowerStatsTest {

    @Test
    void shouldUpdatePowerStatsAttributesTest() {
        PowerStats powerStats = new PowerStats(1, 1, 1, 1);

        powerStats.update(new PowerStats(2, 2, 2, 2));

        assertEquals(2, powerStats.getStrength());
        assertEquals(2, powerStats.getAgility());
        assertEquals(2, powerStats.getDexterity());
        assertEquals(2, powerStats.getIntelligence());
    }

    @Test
    void shouldNotUpdatePowerStatsAttributesWhenValuesAreInvalidTest() {
        PowerStats powerStats = new PowerStats(1, 1, 1, 1);

        powerStats.update(new PowerStats(-1, 11, null, 5)); // Invalid values

        assertEquals(1, powerStats.getStrength());
        assertEquals(1, powerStats.getAgility());
        assertEquals(1, powerStats.getDexterity());
        assertEquals(5, powerStats.getIntelligence());
    }

    @Test
    void shouldUpdatePowerStatsAttributesWithNullValuesTest() {
        PowerStats powerStats = new PowerStats(1, 1, 1, 1);

        powerStats.update(new PowerStats(null, null, null, null)); // All null values

        assertEquals(1, powerStats.getStrength());
        assertEquals(1, powerStats.getAgility());
        assertEquals(1, powerStats.getDexterity());
        assertEquals(1, powerStats.getIntelligence());
    }

    @Test
    void shouldGetTotalPowerStatsTest() {
        PowerStats powerStats = new PowerStats(2, 3, 4, 5);

        int totalPowerStats = powerStats.getTotal();

        assertEquals(14, totalPowerStats);
    }
}
