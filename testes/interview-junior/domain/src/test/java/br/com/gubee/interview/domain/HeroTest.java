package br.com.gubee.interview.domain;

import br.com.gubee.interview.domain.enums.Race;
import br.com.gubee.interview.domain.model.Comparison;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HeroTest {

    @Test
    void shouldNotAllowUpdateWhenDisabledTest() {

        Hero disabledHero = new Hero("Test", Race.HUMAN, new PowerStats(1, 1, 1, 1));
        disabledHero.setEnabled(false);

        assertThrows(IllegalStateException.class, () -> {
            disabledHero.update("New Name", Race.ALIEN, new PowerStats(2, 2, 2, 2));
        });
    }

    @Test
    void shouldUpdateHeroAttributesTest() {
        Hero hero = new Hero("Test", Race.HUMAN, new PowerStats(1, 1, 1, 1));

        hero.update("New Name", Race.ALIEN, new PowerStats(2, 2, 2, 2));

        assertEquals("New Name", hero.getName());
        assertEquals(Race.ALIEN, hero.getRace());
        assertEquals(hero.getPowerStats().getAgility(), 2);
        assertEquals(hero.getPowerStats().getStrength(), 2);
        assertEquals(hero.getPowerStats().getIntelligence(), 2);
        assertEquals(hero.getPowerStats().getDexterity(), 2);
    }

    @Test
    void shouldCompareTwoHeroesAttributesTest() {
        Hero heroOne = new Hero("Test", Race.HUMAN, new PowerStats(6, 3, 7, 4));
        Hero heroTwo = new Hero("Test", Race.ALIEN, new PowerStats(3, 5, 2, 4));

        Comparison comparisonResult = heroOne.compareStatsWith(heroTwo);

        assertEquals(-2, comparisonResult.agility());
        assertEquals(5, comparisonResult.dexterity());
        assertEquals(3, comparisonResult.strength());
        assertEquals(0, comparisonResult.intelligence());
    }

    @Test
    void shouldUpdateHeroAttributesWithNullValuesTest() {
        Hero hero = new Hero("Test", Race.HUMAN, new PowerStats(6, 3, 7, 4));

        hero.update(null, null, null);

        assertEquals("Test", hero.getName());
        assertEquals(Race.HUMAN, hero.getRace());
        assertEquals(hero.getPowerStats().getAgility(), 3);
        assertEquals(hero.getPowerStats().getStrength(), 6);
        assertEquals(hero.getPowerStats().getIntelligence(), 4);
        assertEquals(hero.getPowerStats().getDexterity(), 7);

    }

    @Test
    void shouldUpdateHeroAttributesWithValuesTest() {
        Hero hero = new Hero("Test", Race.HUMAN, new PowerStats(6, 3, 7, 4));

        hero.update("New Test", Race.CYBORG, null);

        assertEquals("New Test", hero.getName());
        assertEquals(Race.CYBORG, hero.getRace());
        assertEquals(hero.getPowerStats().getAgility(), 3);
        assertEquals(hero.getPowerStats().getStrength(), 6);
        assertEquals(hero.getPowerStats().getIntelligence(), 4);
        assertEquals(hero.getPowerStats().getDexterity(), 7);

    }

    @Test
    void shouldUpdateHeroPowerStatsAttributesWithValuesTest() {
        Hero hero = new Hero("Test", Race.HUMAN, new PowerStats(6, 3, 7, 4));

        hero.update("Test", Race.HUMAN, new PowerStats(5, 2, 8, 4));

        assertEquals("Test", hero.getName());
        assertEquals(Race.HUMAN, hero.getRace());
        assertEquals(hero.getPowerStats().getAgility(), 2);
        assertEquals(hero.getPowerStats().getStrength(), 5);
        assertEquals(hero.getPowerStats().getIntelligence(), 4);
        assertEquals(hero.getPowerStats().getDexterity(), 8);
    }

}