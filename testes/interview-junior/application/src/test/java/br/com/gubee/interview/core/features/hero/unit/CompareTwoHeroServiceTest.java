package br.com.gubee.interview.core.features.hero.unit;

import br.com.gubee.interview.application.port.in.ComparisonResult;
import br.com.gubee.interview.application.port.out.HeroQueryPort;
import br.com.gubee.interview.application.service.CompareTwoHeroService;
import br.com.gubee.interview.domain.enums.Race;
import br.com.gubee.interview.domain.model.Comparison;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.UUID;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompareTwoHeroServiceTest {

    @InjectMocks
    private CompareTwoHeroService compareTwoHeroService;

    @Mock
    private HeroQueryPort heroQueryPort;

    @Test
    void shouldCompareTwoHeroesTest() {

        UUID heroIdOne = UUID.randomUUID();
        UUID heroIdTwo = UUID.randomUUID();

        Hero heroOne = new Hero("Superman", Race.ALIEN, new PowerStats(10, 10, 10, 10));
        Hero heroTwo = new Hero("Batman", Race.HUMAN, new PowerStats(5, 10, 10, 10));

        when(heroQueryPort.findById(heroIdOne)).thenReturn(java.util.Optional.of(heroOne));
        when(heroQueryPort.findById(heroIdTwo)).thenReturn(java.util.Optional.of(heroTwo));

        ComparisonResult comparisonResult = compareTwoHeroService.compare(heroIdOne, heroIdTwo);

        assertThat(comparisonResult.comparison().strength()).isEqualTo(5);
        assertThat(comparisonResult.comparison().dexterity()).isEqualTo(0);
        assertThat(comparisonResult.comparison().agility()).isEqualTo(0);
        assertThat(comparisonResult.comparison().intelligence()).isEqualTo(0);
        assertThat(comparisonResult.winnerHeroName()).isEqualTo(heroOne.getName());

    }
}
