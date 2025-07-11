package br.com.gubee.interview.core.features.hero.integration;

import br.com.gubee.interview.domain.dtos.HeroToUpdateDTO;
import br.com.gubee.interview.domain.enums.Race;
import br.com.gubee.interview.domain.dtos.CreateHeroRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = br.com.gubee.interview.application.Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Testcontainers
public class HeroControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    void cleanupDatabase() {
        // Limpa as tabelas antes de cada teste para garantir isolamento
        jdbcTemplate.execute("TRUNCATE TABLE hero, power_stats RESTART IDENTITY CASCADE");
    }

    @Test
    @DisplayName("Deve criar um herói com sucesso e retornar status 201 Created com Location Header")
    void shouldCreateHeroSuccessfullyAndReturn201Created() throws Exception {
        CreateHeroRequest heroRequest = CreateHeroRequest.builder()
                .name("Superman")
                .race(Race.ALIEN)
                .strength(10)
                .agility(8)
                .dexterity(7)
                .intelligence(9)
                .build();

        String heroRequestJson = objectMapper.writeValueAsString(heroRequest);

        mockMvc.perform(post("/api/v1/heroes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(heroRequestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", matchesPattern(".*/api/v1/heroes/[0-9a-f\\-]{36}$")));
    }

    @Test
    @DisplayName("Deve encontrar um herói por ID e retornar status 200 OK com os dados completos")
    void shouldFindHeroByIdAndReturn200Ok() throws Exception {

        UUID createdHeroId = createHeroViaApi("Wonder Woman", Race.DIVINE, 10);

        mockMvc.perform(get("/api/v1/heroes/{id}", createdHeroId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(createdHeroId.toString())))
                .andExpect(jsonPath("$.name", is("Wonder Woman")))
                .andExpect(jsonPath("$.race", is("DIVINE")))
                .andExpect(jsonPath("$.strength", is(10)))
                .andExpect(jsonPath("$.agility", is(5)))
                .andExpect(jsonPath("$.dexterity", is(5)))
                .andExpect(jsonPath("$.intelligence", is(5)));
    }

    @Test
    @DisplayName("Deve encontrar heróis por nome parcial e retornar lista de DTOs completos")
    void shouldFindHeroesByPartialNameAndReturnFullDtoList() throws Exception {
        // Arrange: Criar múltiplos heróis, alguns que correspondem à busca e outros não
        createHeroViaApi("Batman", Race.HUMAN, 5);
        createHeroViaApi("Batgirl", Race.HUMAN, 6);
        createHeroViaApi("Superman", Race.ALIEN, 10);

        // Act & Assert
        mockMvc.perform(get("/api/v1/heroes/search")
                        .param("name", "Bat")) // Busca por um nome parcial
                .andExpect(status().isOk()) // Espera 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2))) // Espera uma lista com 2 resultados
                // Verifica se os nomes corretos estão na resposta, em qualquer ordem
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Batman", "Batgirl")))
                // Verifica um atributo de cada para garantir que os PowerStats foram buscados
                .andExpect(jsonPath("$[?(@.name=='Batman')].strength", contains(5)))
                .andExpect(jsonPath("$[?(@.name=='Batgirl')].strength", contains(6)));
    }

    @Test
    @DisplayName("Deve buscar herói que não existe por nome e retornar uma lista vazia")
    void shouldntFindHeroesByNameAndReturnEmptyDtoList() throws Exception {

        createHeroViaApi("Batman", Race.HUMAN, 5);
        createHeroViaApi("Batgirl", Race.HUMAN, 6);
        createHeroViaApi("Superman", Race.ALIEN, 10);

        mockMvc.perform(get("/api/v1/heroes/search")
                        .param("name", "Lanterna Verde")) // Busca por um nome que não existe
                .andExpect(status().isOk()) // Espera 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(0))); // Espera uma lista vazia
    }

    @Test
    @DisplayName("Deve deletar herói por ID")
    void shouldDeleteHeroById() throws Exception {

        UUID createdHeroId = createHeroViaApi("Batman", Race.HUMAN, 9);

        mockMvc.perform(delete("/api/v1/heroes/{id}", createdHeroId)) // Busca por um nome que não existe
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)); // Espera uma lista vazia

        mockMvc.perform(get("/api/v1/heroes/{id}", createdHeroId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar um herói que não existe por ID e retornar status NOT_FOUND")
    void shouldDeleteHeroByIdAndReturnNotFound() throws Exception {
        UUID randomUUID = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/heroes/{id}", randomUUID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar um herói pelo ID")
    void shoulUpdateHeroeById() throws Exception {

        UUID createdHeroId = createHeroViaApi("Batman", Race.HUMAN, 9);

        HeroToUpdateDTO heroToUpdateDTO = new HeroToUpdateDTO("Batman Updated",
                null,
                10,
                6,
                7,
                8
        );

        String updateHeroJson = objectMapper.writeValueAsString(heroToUpdateDTO);

        mockMvc.perform(put("/api/v1/heroes/{id}", createdHeroId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateHeroJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)); // Espera uma lista vazia

        mockMvc.perform(get("/api/v1/heroes/{id}", createdHeroId))
                .andExpect(jsonPath("$.name", is("Batman Updated")))
                .andExpect(jsonPath("$.race", is("HUMAN")))
                .andExpect(jsonPath("$.agility", is(10)))
                .andExpect(jsonPath("$.dexterity", is(6)))
                .andExpect(jsonPath("$.strength", is(7)))
                .andExpect(jsonPath("$.intelligence", is(8)));
    }

    @Test
    @DisplayName("Deve comparar dois heróis e retornar um objeto contendo os powerstats de cada herói e sua comparação")
    void shouldCompareTwoHeroStatsAndReturn() throws Exception {

        UUID firstCreatedHeroId = createHeroViaApi("Batman", Race.HUMAN, 9);
        UUID secondCreatedHeroId = createHeroViaApi("Superman", Race.ALIEN, 10);

        mockMvc.perform(get("/api/v1/heroes/compare")
                        .param("idHeroOne", firstCreatedHeroId.toString())
                        .param("idHeroTwo", secondCreatedHeroId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.hero_one.id", is(firstCreatedHeroId.toString())))
                .andExpect(jsonPath("$.hero_one.name", is("Batman")))
                .andExpect(jsonPath("$.hero_one.race", is("HUMAN")))
                .andExpect(jsonPath("$.hero_one.agility", is(5)))
                .andExpect(jsonPath("$.hero_one.dexterity", is(5)))
                .andExpect(jsonPath("$.hero_one.strength", is(9)))
                .andExpect(jsonPath("$.hero_one.intelligence", is(5)))
                // Verifica os dados do segundo herói
                .andExpect(jsonPath("$.hero_two.id", is(secondCreatedHeroId.toString())))
                .andExpect(jsonPath("$.hero_two.name", is("Superman")))
                .andExpect(jsonPath("$.hero_two.race", is("ALIEN")))
                .andExpect(jsonPath("$.hero_two.agility", is(5)))
                .andExpect(jsonPath("$.hero_two.dexterity", is(5)))
                .andExpect(jsonPath("$.hero_two.strength", is(10)))
                .andExpect(jsonPath("$.hero_two.intelligence", is(5)))
                // Verifica o resultado da comparação
                .andExpect(jsonPath("$.comparison.strength", is(-1)))
                .andExpect(jsonPath("$.comparison.agility", is(0)))
                .andExpect(jsonPath("$.comparison.dexterity", is(0)))
                .andExpect(jsonPath("$.comparison.intelligence", is(0)));

    }

    // Método auxiliar para criar heróis via API e reduzir duplicação de código
    private UUID createHeroViaApi(String name, Race race, int strength) throws Exception {
        CreateHeroRequest request = CreateHeroRequest.builder()
                .name(name)
                .race(race)
                .strength(strength)
                .agility(5).dexterity(5).intelligence(5)
                .build();

        MvcResult createResult = mockMvc.perform(post("/api/v1/heroes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String locationHeader = createResult.getResponse().getHeader("Location");
        return UUID.fromString(locationHeader.substring(locationHeader.lastIndexOf('/') + 1));
    }
}
