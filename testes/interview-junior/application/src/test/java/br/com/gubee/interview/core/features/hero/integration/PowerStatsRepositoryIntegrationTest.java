package br.com.gubee.interview.core.features.hero.integration;

import br.com.gubee.interview.domain.model.PowerStats;
import br.com.gubee.interview.infrastructure.features.powerstats.PowerStatsRepositoryImpl;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest(classes = br.com.gubee.interview.application.Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Testcontainers
public class PowerStatsRepositoryIntegrationTest {

    @Autowired
    private PowerStatsRepositoryImpl powerStatsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = postgresContainer.getJdbcUrl() + "?currentSchema=interview_service";
        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.flyway.schemas", () -> "interview_service");
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE hero, power_stats RESTART IDENTITY CASCADE");
    }

    @Test
    @DisplayName("create deve salvar PowerStats e retornar o ID gerado")
    void createShouldSavePowerStatsAndReturnId() {

        PowerStats powerStats = PowerStats.builder()
                .strength(10)
                .agility(8)
                .dexterity(7)
                .intelligence(9)
                .build();

        UUID createdId = powerStatsRepository.create(powerStats);

        assertThat(createdId).isNotNull();

        Optional<PowerStats> foundStats = powerStatsRepository.findById(createdId);
        assertThat(foundStats).isPresent();
        assertThat(foundStats.get().getStrength()).isEqualTo(10);
    }

    @Test
    @DisplayName("findById deve retornar PowerStats quando o ID existe")
    void findByIdShouldReturnPowerStats_WhenIdExists() {

        PowerStats statsToCreate = PowerStats.builder().strength(7).build();
        UUID createdId = powerStatsRepository.create(statsToCreate);

        Optional<PowerStats> foundStatsOptional = powerStatsRepository.findById(createdId);

        assertThat(foundStatsOptional).isPresent();
        assertThat(foundStatsOptional.get().getId()).isEqualTo(createdId);
        assertThat(foundStatsOptional.get().getStrength()).isEqualTo(7);
    }

    @Test
    @DisplayName("findById deve retornar Optional vazio quando o ID não existe")
    void findByIdShouldReturnEmpty_WhenIdDoesNotExist() {

        Optional<PowerStats> result = powerStatsRepository.findById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("update deve modificar os dados de PowerStats no banco")
    void updateShouldModifyPowerStatsDataInDatabase() {

        PowerStats stats = PowerStats.builder().strength(1).agility(1).dexterity(1).intelligence(1).build();
        UUID createdId = powerStatsRepository.create(stats);

        stats.setId(createdId);
        stats.setStrength(10);
        stats.setAgility(10);

        int rowsAffected = powerStatsRepository.update(stats);

        assertThat(rowsAffected).isEqualTo(1);

        Optional<PowerStats> updatedStats = powerStatsRepository.findById(createdId);
        assertThat(updatedStats).isPresent();
        assertThat(updatedStats.get().getStrength()).isEqualTo(10);
        assertThat(updatedStats.get().getAgility()).isEqualTo(10);
    }

    @Test
    @DisplayName("deleteById deve remover PowerStats do banco")
    void deleteByIdShouldRemovePowerStatsFromDatabase() {

        UUID createdId = powerStatsRepository.create(PowerStats.builder().strength(1).build());

        powerStatsRepository.deleteById(createdId);

        Optional<PowerStats> result = powerStatsRepository.findById(createdId);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("deleteById deve falhar se PowerStats estiver em uso por um Hero")
    void deleteByIdShouldFailWhenPowerStatsIsInUse() {

        UUID powerStatsId = powerStatsRepository.create(PowerStats.builder().strength(1).build());

        jdbcTemplate.update(
                "INSERT INTO hero (id, name, race, power_stats_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)",
                UUID.randomUUID(), "Test Hero", "HUMAN", powerStatsId, new java.sql.Timestamp(Instant.now().toEpochMilli()), new java.sql.Timestamp(Instant.now().toEpochMilli())
        );

        assertThatThrownBy(() -> powerStatsRepository.deleteById(powerStatsId))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
