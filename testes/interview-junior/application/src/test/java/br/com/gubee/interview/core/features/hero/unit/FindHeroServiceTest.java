package br.com.gubee.interview.core.features.hero.unit;

import br.com.gubee.interview.application.port.out.HeroQueryPort;
import br.com.gubee.interview.application.service.FindHeroService;
import br.com.gubee.interview.domain.enums.Race;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindHeroServiceTest {

    @InjectMocks
    private FindHeroService findHeroService;

    @Mock
    private HeroQueryPort heroQueryPort;

    @Test
    void shouldSearchByHeroNameTest() {

        Hero heroOne = new Hero("Superman", Race.ALIEN, new PowerStats(10, 10, 10, 10));
        Hero heroTwo = new Hero("Batman", Race.HUMAN, new PowerStats(5, 10, 10, 10));

        when(heroQueryPort.searchHeroByName("Superman")).thenReturn(List.of(heroOne));
        when(heroQueryPort.searchHeroByName("Batman")).thenReturn(List.of(heroTwo));
        when(heroQueryPort.searchHeroByName("")).thenReturn(List.of(heroOne, heroTwo));

        List<Hero> heroesByName = findHeroService.searchByHeroName("Superman");
        assert heroesByName.size() == 1;
        assert heroesByName.get(0).getName().equals("Superman");

        heroesByName = findHeroService.searchByHeroName("Batman");
        assert heroesByName.size() == 1;
        assert heroesByName.get(0).getName().equals("Batman");

        heroesByName = findHeroService.searchByHeroName("");
        assert heroesByName.size() == 2;

        verify(heroQueryPort, times(3)).searchHeroByName(any(String.class));

    }

    @Test
    void shouldFindHeroByIdTest() {

        UUID heroId = UUID.randomUUID();

        Hero heroOne = new Hero("Superman", Race.ALIEN, new PowerStats(10, 10, 10, 10));

        when(heroQueryPort.findById(heroId)).thenReturn(java.util.Optional.of(heroOne));

        Hero foundHero = findHeroService.findById(heroId);
        assertThat(foundHero).isNotNull();

        verify(heroQueryPort).findById(heroId);
        verify(heroQueryPort, times(1)).findById(heroId);
    }
}
