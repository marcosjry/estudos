package br.com.gubee.interview.infrastructure.features.DTO;

import java.util.UUID;

public record HeroToRequestDTO(
        UUID id,
        String name,
        String race,
        Integer agility,
        Integer dexterity,
        Integer strength,
        Integer intelligence) {
}
