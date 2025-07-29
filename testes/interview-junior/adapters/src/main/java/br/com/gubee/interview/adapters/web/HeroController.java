package br.com.gubee.interview.infrastructure.adapter.in.web;

import br.com.gubee.interview.application.port.in.*;
import br.com.gubee.interview.domain.model.CommandHero;
import br.com.gubee.interview.infrastructure.DTO.ComparedHeroesDTO;
import br.com.gubee.interview.infrastructure.DTO.CreateHeroRequest;
import br.com.gubee.interview.infrastructure.DTO.HeroToRequestDTO;
import br.com.gubee.interview.domain.model.ComparisonResult;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.infrastructure.adapter.in.web.mapper.HeroMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
public class HeroController {

    private final HeroMapper heroMapper;

    private final CreateHeroUseCase createHeroUseCase;
    private final FindHeroUseCase findHeroUseCase;
    private final DeleteHeroUseCase deleteHeroUseCase;
    private final UpdateHeroUseCase updateHeroUseCase;
    private final CompareTwoHeroUseCase compareTwoHeroUseCase;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@Validated @RequestBody CreateHeroRequest createHeroRequest) {

        final CommandHero commandHero = this.heroMapper.toCommand(createHeroRequest);

        final UUID id = this.createHeroUseCase.createHero(commandHero);

        return created(URI.create(format("/api/v1/heroes/%s", id))).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<HeroToRequestDTO> findHeroById(@Validated @PathVariable UUID id) {
        final Hero hero = this.findHeroUseCase.findById(id);

        final HeroToRequestDTO heroDto = this.heroMapper.toDto(hero);

        return ResponseEntity.ok()
                .header("Content-Type", APPLICATION_JSON_VALUE)
                .body(heroDto);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<HeroToRequestDTO>> searchHeroByName(@RequestParam String name) {

        List<Hero> heroes = this.findHeroUseCase.searchByHeroName(name);

        List<HeroToRequestDTO> heroesDto = this.heroMapper.toDto(heroes);

        return ResponseEntity.ok()
                .header("Content-Type", APPLICATION_JSON_VALUE)
                .body(heroesDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteHeroByName(@PathVariable UUID id) {
        this.deleteHeroUseCase.deleteHeroById(id);
        return ResponseEntity.ok()
                .header("Content-Type", APPLICATION_JSON_VALUE)
                .build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateHero(@PathVariable UUID id, @RequestBody CommandHero heroToUpdateDTO) {

        this.updateHeroUseCase.updateHeroById(id, heroToUpdateDTO);

        return ResponseEntity.ok()
                .header("Content-Type", APPLICATION_JSON_VALUE)
                .build();
    }

    @GetMapping(value = "/compare")
    public ResponseEntity<ComparedHeroesDTO> CompareTwoHeroes(@RequestParam UUID idHeroOne, @RequestParam UUID idHeroTwo) {
        final ComparisonResult result = this.compareTwoHeroUseCase.compare(idHeroOne, idHeroTwo);

        final ComparedHeroesDTO resultDto = this.heroMapper.toDto(result);

        return ResponseEntity.ok()
                .header("Content-Type", APPLICATION_JSON_VALUE)
                .body(resultDto);
    }
}
