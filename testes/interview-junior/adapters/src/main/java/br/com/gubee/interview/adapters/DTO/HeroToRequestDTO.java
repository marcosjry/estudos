package br.com.gubee.interview.adapters.DTO;

import br.com.gubee.interview.domain.enums.Race;

import java.util.UUID;

public record HeroToRequestDTO(
        UUID id,
        String name,
        Race race,
        Integer agility,
        Integer dexterity,
        Integer strength,
        Integer intelligence) {
}
