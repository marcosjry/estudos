package br.com.gubee.interview.adapters;

import br.com.gubee.interview.adapters.DTO.CreateHeroRequest;
import br.com.gubee.interview.domain.enums.Race;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HeroE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    void shouldCreateHeroTest() {

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
    void shouldReturnNotFoundForNonExistentHeroTest() {

        UUID randomUUID = UUID.randomUUID();
        String url = "http://localhost:" + port + "/api/v1/heroes/" + randomUUID;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
