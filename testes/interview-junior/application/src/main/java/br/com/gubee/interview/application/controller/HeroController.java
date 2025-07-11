package br.com.gubee.interview.application.controller;

import br.com.gubee.interview.domain.dtos.CompareHero;
import br.com.gubee.interview.domain.dtos.HeroToRequestDTO;
import br.com.gubee.interview.domain.dtos.HeroToUpdateDTO;
import br.com.gubee.interview.domain.dtos.CreateHeroRequest;
import br.com.gubee.interview.domain.service.HeroService;
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

    private final HeroService heroService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@Validated @RequestBody CreateHeroRequest createHeroRequest) {
        final UUID id = heroService.createNewHero(createHeroRequest);
        return created(URI.create(format("/api/v1/heroes/%s", id))).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<HeroToRequestDTO> findHeroById(@Validated @PathVariable UUID id) {
        final HeroToRequestDTO hero = heroService.findHeroByIdToRequestDTO(id);
        return ResponseEntity.ok()
                .header("Content-Type", APPLICATION_JSON_VALUE)
                .body(hero);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<HeroToRequestDTO>> searchHeroByName(@RequestParam String name) {
        final List<HeroToRequestDTO> hero = heroService.searchByHeroName(name);
        return ResponseEntity.ok()
                .header("Content-Type", APPLICATION_JSON_VALUE)
                .body(hero);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteHeroByName(@PathVariable UUID id) {
        heroService.deleteHeroById(id);
        return ResponseEntity.ok()
                .header("Content-Type", APPLICATION_JSON_VALUE)
                .build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateHero(@PathVariable UUID id, @RequestBody HeroToUpdateDTO heroToUpdateDTO) {
        heroService.updateHeroById(id, heroToUpdateDTO);
        return ResponseEntity.ok()
                .header("Content-Type", APPLICATION_JSON_VALUE)
                .build();
    }

    @GetMapping(value = "/compare")
    public ResponseEntity<CompareHero> CompareTwoHeroes(@RequestParam UUID idHeroOne, @RequestParam UUID idHeroTwo) {
        CompareHero result = heroService.compareTwoHeroes(idHeroOne, idHeroTwo);
        return ResponseEntity.ok()
                .header("Content-Type", APPLICATION_JSON_VALUE)
                .body(result);
    }


}
