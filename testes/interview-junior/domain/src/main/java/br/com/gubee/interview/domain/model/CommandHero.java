package br.com.gubee.interview.domain.model;

import br.com.gubee.interview.domain.enums.Race;

public record CommandHero(
        String name,
        Race race,
        Integer strength,
        Integer agility,
        Integer dexterity,
        Integer intelligence
) {
}
