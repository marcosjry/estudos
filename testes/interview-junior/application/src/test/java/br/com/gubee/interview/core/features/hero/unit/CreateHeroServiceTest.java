package br.com.gubee.interview.core.features.hero.unit;

import br.com.gubee.interview.application.port.in.CommandHero;
import br.com.gubee.interview.application.port.out.HeroCommandPort;
import br.com.gubee.interview.application.service.CreateHeroService;
import br.com.gubee.interview.domain.enums.Race;
import br.com.gubee.interview.domain.exception.HeroNameAlreadyExistsException;
import br.com.gubee.interview.domain.model.Hero;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateHeroServiceTest {

    @InjectMocks
    private CreateHeroService createHeroService;

    @Mock
    private HeroCommandPort heroCommandPort;

    @Test
    void shouldCreateHeroTest() {

        CommandHero command = new CommandHero("Superman", Race.ALIEN, 10, 10, 10, 10);
        when(heroCommandPort.create(any(Hero.class))).thenReturn(UUID.randomUUID());

        UUID heroId = createHeroService.createHero(command);

        assertNotNull(heroId);
        verify(heroCommandPort, times(1)).create(any(Hero.class));
    }

    @Test
    void shouldThrowExceptionWhenHeroNameAlreadyExistsTest() {

        CommandHero command = new CommandHero("Superman", Race.ALIEN, 10, 10, 10, 10);

        String errorMessage = "Hero with name 'Superman' already exists.";

        when(heroCommandPort.create(any(Hero.class)))
                .thenThrow(new HeroNameAlreadyExistsException(errorMessage));

        HeroNameAlreadyExistsException exception = assertThrows(
                HeroNameAlreadyExistsException.class,
                () -> createHeroService.createHero(command)
        );

        assertThat(exception.getMessage()).isEqualTo(errorMessage);

        verify(heroCommandPort, times(1)).create(any(Hero.class));
    }
}
