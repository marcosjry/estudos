package br.com.gubee.interview.core.features.hero.e2e;

import br.com.gubee.interview.domain.dtos.HeroToRequestDTO;
import br.com.gubee.interview.domain.enums.Race;
import br.com.gubee.interview.domain.dtos.CreateHeroRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.HttpStatus;
import com.jayway.jsonpath.JsonPath;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = br.com.gubee.interview.application.Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("it")
public class HeroEndToEndTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("jdbc.url", postgresContainer::getJdbcUrl);
        registry.add("jdbc.username", postgresContainer::getUsername);
        registry.add("jdbc.password", postgresContainer::getPassword);
        registry.add("jdbc.schema", () -> "interview_service");
    }

    @Test
    @DisplayName("E2E: Deve criar um herói com sucesso e retornar status 201 Created")
    void shouldCreateHero() {

        String baseUrl = "http://localhost:" + port + "/api/v1/heroes";

        CreateHeroRequest heroRequest = CreateHeroRequest.builder()
                .name("The Flash")
                .race(Race.HUMAN)
                .strength(7)
                .agility(10)
                .dexterity(9)
                .intelligence(6)
                .build();

        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl,
                heroRequest,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI location = response.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(location.toString()).matches("/api/v1/heroes/[0-9a-f\\-]{36}$");
    }

    @Test
    @DisplayName("E2E: Deve encontrar um herói por ID e retornar os dados corretos")
    void shouldFindHeroByIdSuccessfullyE2E() {

        UUID heroId = createHeroViaApi("Green Arrow", Race.HUMAN, 6, 8);
        String url = "http://localhost:" + port + "/api/v1/heroes/" + heroId;

        ResponseEntity<HeroToRequestDTO> response = restTemplate.getForEntity(url, HeroToRequestDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        HeroToRequestDTO responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.id()).isEqualTo(heroId);
        assertThat(responseBody.name()).isEqualTo("Green Arrow");
        assertThat(responseBody.strength()).isEqualTo(4);
        assertThat(responseBody.agility()).isEqualTo(8);
    }

    @Test
    @DisplayName("E2E: Deve retornar 404 Not Found ao buscar um herói com ID inexistente")
    void shouldReturn404ForNonExistentHeroE2E() {

        UUID randomUUID = UUID.randomUUID();
        String url = "http://localhost:" + port + "/api/v1/heroes/" + randomUUID;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("E2E: Deve comparar dois heróis e retornar a diferença de atributos")
    void shouldCompareHeroesAndReturnCorrectDifferencesE2E() {

        UUID hero1Id = createHeroViaApi("Batman", Race.HUMAN, 8, 8);
        UUID hero2Id = createHeroViaApi("Superman", Race.ALIEN, 10, 7);

        String url = String.format("http://localhost:%d/api/v1/heroes/compare?idHeroOne=%s&idHeroTwo=%s", port, hero1Id, hero2Id);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        String responseBody = response.getBody();
        assertThat(responseBody).isNotNull();

        // Usando JsonPath para verificar os valores na resposta JSON
        assertThat((String) JsonPath.read(responseBody, "$.hero_one.name")).isEqualTo("Batman");
        assertThat((String) JsonPath.read(responseBody, "$.hero_two.name")).isEqualTo("Superman");
        assertThat((Integer) JsonPath.read(responseBody, "$.comparison.strength")).isEqualTo(-2);
        assertThat((Integer) JsonPath.read(responseBody, "$.comparison.agility")).isEqualTo(1);
    }

    // --- MÉTODO AUXILIAR PARA CRIAR HERÓI ---
    private UUID createHeroViaApi(String name, Race race, int strength, int agility) {
        String baseUrl = "http://localhost:" + port + "/api/v1/heroes";
        CreateHeroRequest heroRequest = CreateHeroRequest.builder()
                .name(name)
                .race(race)
                .strength(strength)
                .agility(agility)
                .dexterity(7)
                .intelligence(8)
                .build();

        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl, heroRequest, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI location = response.getHeaders().getLocation();
        assertThat(location).isNotNull();
        String path = location.getPath();
        return UUID.fromString(path.substring(path.lastIndexOf('/') + 1));
    }
}
