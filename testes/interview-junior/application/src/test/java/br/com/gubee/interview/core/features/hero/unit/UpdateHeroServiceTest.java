package br.com.gubee.interview.core.features.hero.unit;

import br.com.gubee.interview.application.port.in.CommandHero;
import br.com.gubee.interview.application.port.out.HeroCommandPort;
import br.com.gubee.interview.application.port.out.HeroQueryPort;
import br.com.gubee.interview.application.service.UpdateHeroService;
import br.com.gubee.interview.domain.enums.Race;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateHeroServiceTest {

    @InjectMocks
    private UpdateHeroService updateHeroService;

    @Mock
    private HeroCommandPort heroCommandPort;

    @Mock
    private HeroQueryPort heroQueryPort;

    @Test
    void shouldUpdateHeroTest() {

        UUID heroId = UUID.randomUUID();
        Hero hero = new Hero("Superman", Race.ALIEN, new PowerStats(10,10,10,10));

        when(heroQueryPort.findById(heroId)).thenReturn(Optional.of(hero));
        when(heroCommandPort.update(hero)).thenReturn(1);

        CommandHero heroToUpdate = new CommandHero(
                "Superman Novo",
                null,
                5,
                5,
                5,
                1
        );

        updateHeroService.updateHeroById(heroId, heroToUpdate);

        verify(heroCommandPort).update(hero);

        assertEquals("Superman Novo", hero.getName());
        assertEquals(Race.ALIEN, hero.getRace());
        assertEquals(5, hero.getPowerStats().getStrength());
        assertEquals(5, hero.getPowerStats().getAgility());
        assertEquals(5, hero.getPowerStats().getDexterity());
        assertEquals(1, hero.getPowerStats().getIntelligence());

    }
}
