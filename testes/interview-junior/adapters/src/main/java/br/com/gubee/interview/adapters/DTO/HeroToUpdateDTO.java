package br.com.gubee.interview.infrastructure.DTO;

import br.com.gubee.interview.domain.enums.Race;

public record HeroToUpdateDTO(
        String name,
        Race race,
        Integer agility,
        Integer dexterity,
        Integer strength,
        Integer intelligence
) {
}
