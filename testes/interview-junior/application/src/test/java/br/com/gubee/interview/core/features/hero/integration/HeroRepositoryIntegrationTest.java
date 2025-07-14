package br.com.gubee.interview.core.features.hero.integration;

import br.com.gubee.interview.domain.enums.Race;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.infrastructure.features.hero.HeroRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = br.com.gubee.interview.application.Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Testcontainers
public class HeroRepositoryIntegrationTest {
    @Autowired
    private HeroRepositoryImpl heroRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {

        String jdbcUrl = postgresContainer.getJdbcUrl() + "?currentSchema=interview_service";

        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.flyway.schemas", () -> "interview_service");
    }

    private UUID powerStatsId;

    @BeforeEach
    void setUp() {

        jdbcTemplate.execute("TRUNCATE TABLE hero, power_stats RESTART IDENTITY CASCADE");

        powerStatsId = UUID.randomUUID();
        jdbcTemplate.update(
                "INSERT INTO power_stats (id, strength, agility, dexterity, intelligence, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
                powerStatsId, 5, 5, 5, 5, new java.sql.Timestamp(Instant.now().toEpochMilli()), new java.sql.Timestamp(Instant.now().toEpochMilli())
        );
    }

    @Test
    @DisplayName("create deve salvar um herói no banco e retornar seu ID")
    void createShouldSaveHeroAndReturnId() {

        Hero hero = Hero.builder()
                .name("Green Lantern")
                .race(Race.ALIEN)
                .powerStatsId(powerStatsId)
                .build();


        UUID createdId = heroRepository.create(hero);


        assertThat(createdId).isNotNull();

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM hero WHERE id = ?", Integer.class, createdId);
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("findById deve retornar o herói quando o ID existe")
    void findByIdShouldReturnHero_WhenIdExists() {
        Hero heroToCreate = Hero.builder().name("The Flash").race(Race.HUMAN).powerStatsId(powerStatsId).build();
        UUID createdId = heroRepository.create(heroToCreate);

        Optional<Hero> foundHeroOptional = heroRepository.findById(createdId);

        assertThat(foundHeroOptional).isPresent();
        assertThat(foundHeroOptional.get().getId()).isEqualTo(createdId);
        assertThat(foundHeroOptional.get().getName()).isEqualTo("The Flash");
    }

    @Test
    @DisplayName("findById deve retornar Optional vazio quando o ID não existe")
    void findByIdShouldReturnEmptyWhenIdDoesNotExist() {
        Optional<Hero> result = heroRepository.findById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("searchByHeroName deve encontrar heróis por nome parcial")
    void searchByHeroNameShouldFindHeroesByPartialName() {
        heroRepository.create(Hero.builder().name("Batman").race(Race.HUMAN).powerStatsId(powerStatsId).build());
        heroRepository.create(Hero.builder().name("Batgirl").race(Race.HUMAN).powerStatsId(powerStatsId).build());
        heroRepository.create(Hero.builder().name("Superman").race(Race.ALIEN).powerStatsId(powerStatsId).build());

        List<Hero> foundHeroes = heroRepository.searchByHeroName("Bat");

        assertThat(foundHeroes).hasSize(2);
        assertThat(foundHeroes).extracting(Hero::getName).containsExactlyInAnyOrder("Batman", "Batgirl");
    }

    @Test
    @DisplayName("update deve modificar os dados do herói no banco")
    void updateShouldModifyHeroDataInDatabase() {

        Hero hero = Hero.builder().name("Clark Kent").race(Race.ALIEN).powerStatsId(powerStatsId).build();
        UUID createdId = heroRepository.create(hero);

        hero.setId(createdId);
        hero.setName("Superman");
        hero.setRace(Race.ALIEN);

        int rowsAffected = heroRepository.update(hero);

        assertThat(rowsAffected).isEqualTo(1);

        Optional<Hero> updatedHero = heroRepository.findById(createdId);
        assertThat(updatedHero).isPresent();
        assertThat(updatedHero.get().getName()).isEqualTo("Superman");
    }

    @Test
    @DisplayName("deleteHeroById deve remover o herói do banco")
    void deleteHeroByIdShouldRemoveHeroFromDatabase() {

        UUID createdId = heroRepository.create(Hero.builder().name("To be deleted").race(Race.HUMAN).powerStatsId(powerStatsId).build());

        heroRepository.deleteHeroById(createdId);

        Optional<Hero> result = heroRepository.findById(createdId);
        assertThat(result).isEmpty();
    }
}
