package br.com.gubee.interview.core.features.hero.unit;

import br.com.gubee.interview.application.port.out.HeroCommandPort;
import br.com.gubee.interview.application.port.out.HeroQueryPort;
import br.com.gubee.interview.application.service.DeleteHeroService;
import br.com.gubee.interview.domain.enums.Race;
import br.com.gubee.interview.domain.exception.NotFoundException;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteHeroServiceTest {

    @InjectMocks
    private DeleteHeroService deleteHeroService;

    @Mock
    private HeroCommandPort heroCommandPort;

    @Mock
    private HeroQueryPort heroQueryPort;


    @Test
    void shoudlDeleteHeroTest() {

        UUID heroId = UUID.randomUUID();

        Hero hero = new Hero("Superman", Race.ALIEN, new PowerStats(1,1,1,1));

        when(heroQueryPort.findById(heroId)).thenReturn(Optional.of(hero));

        deleteHeroService.deleteHeroById(heroId);

        ArgumentCaptor<Hero> heroCaptor = ArgumentCaptor.forClass(Hero.class);
        verify(heroCommandPort).deleteHero(heroCaptor.capture());

        Hero heroiCapturado = heroCaptor.getValue();
        assertEquals("Superman", heroiCapturado.getName());
        assertEquals(Race.ALIEN, heroiCapturado.getRace());
        assertEquals(1, heroiCapturado.getPowerStats().getStrength());
        assertEquals(1, heroiCapturado.getPowerStats().getAgility());
        assertEquals(1, heroiCapturado.getPowerStats().getDexterity());
        assertEquals(1, heroiCapturado.getPowerStats().getIntelligence());

    }

    @Test
    void shouldTryDeleteHeroAndThrowExceptionNotFoundTest() {

        UUID heroId = UUID.randomUUID();

        String errorMessage = "Hero not found with id: " + heroId;

        when(heroQueryPort.findById(heroId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> deleteHeroService.deleteHeroById(heroId)
        );

        assertThat(exception.getMessage()).isEqualTo(errorMessage);
        verify(heroCommandPort, never()).deleteHero(any(Hero.class));
    }
}
