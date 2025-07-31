package br.com.gubee.interview.adapters;

import br.com.gubee.interview.application.port.out.HeroCommandPort;
import br.com.gubee.interview.application.port.out.HeroQueryPort;
import br.com.gubee.interview.application.port.out.PowerStatsCommandPort;
import br.com.gubee.interview.application.port.out.PowerStatsQueryPort;
import br.com.gubee.interview.domain.enums.Race;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.then;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class HeroPersistenceAdapterTest {

    @Autowired
    private HeroCommandPort heroCommandPort;

    @Autowired
    private HeroQueryPort heroQueryPort;

    @Autowired
    private PowerStatsQueryPort powerStatsQueryPort;

    @Autowired
    private PowerStatsCommandPort powerStatsCommandPort;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanupDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE hero, power_stats RESTART IDENTITY CASCADE");
    }


    @Test
    void createHeroTest() {

        PowerStats powerStats = new PowerStats(
                10,
                20,
                15,
                25
        );

        Hero hero = new Hero("Superman", Race.HUMAN, powerStats);

        UUID heroId = heroCommandPort.create(hero);

        assertThat(heroId).isNotNull();
    }

    @Test
    void createPowerStatsTest() {

        UUID powerStatsId = powerStatsCommandPort.create(
                new PowerStats(10, 20, 15, 25)
        );

        PowerStats findedPowerStats = powerStatsQueryPort.findById(powerStatsId).get();

        assertThat(findedPowerStats).isNotNull();
        then(findedPowerStats.getId()).isEqualTo(powerStatsId);
        then(findedPowerStats.getAgility()).isEqualTo(20);
        then(findedPowerStats.getStrength()).isEqualTo(10);
        then(findedPowerStats.getDexterity()).isEqualTo(15);
        then(findedPowerStats.getIntelligence()).isEqualTo(25);

    }

    @Test
    void findHeroTest() {

        UUID heroId = createHeroAux(
                "Superman",
                Race.ALIEN,
                new PowerStats(10, 20, 15, 25)
        );

        assertThat(heroId).isNotNull();
        Hero findedHero = heroQueryPort.findById(heroId).get();
        assertThat(findedHero).isNotNull();
        then(findedHero.getId()).isEqualTo(heroId);
        then(findedHero.getName()).isEqualTo("Superman");
        then(findedHero.getRace()).isEqualTo(Race.ALIEN);
        then(findedHero.getPowerStats().getId()).isNotNull();
        then(findedHero.getPowerStats().getAgility()).isEqualTo(20);
        then(findedHero.getPowerStats().getStrength()).isEqualTo(10);
        then(findedHero.getPowerStats().getDexterity()).isEqualTo(15);
        then(findedHero.getPowerStats().getIntelligence()).isEqualTo(25);

    }

    @Test
    void shouldNotFindHero() {

        UUID heroId = UUID.randomUUID();

        assertThat(heroId).isNotNull();

        assertThat(heroQueryPort.findById(heroId)).isEmpty();

    }



    @Test
    void deleteHeroTest() {

        UUID heroId = createHeroAux(
                "Superman",
                Race.ALIEN,
                new PowerStats(10, 20, 15, 25)
        );

        Hero findedHero = heroQueryPort.findById(heroId).get();
        assertThat(findedHero).isNotNull();
        then(findedHero.getId()).isEqualTo(heroId);
        then(findedHero.getName()).isEqualTo("Superman");
        then(findedHero.getRace()).isEqualTo(Race.ALIEN);

        heroCommandPort.deleteHero(findedHero);

        then(heroQueryPort.findById(heroId)).isEmpty();


    }

    private UUID createHeroAux(String heroName, Race race, PowerStats powerStats) {
        Hero hero = new Hero(heroName, race, powerStats);
        return heroCommandPort.create(hero);
    }


}
