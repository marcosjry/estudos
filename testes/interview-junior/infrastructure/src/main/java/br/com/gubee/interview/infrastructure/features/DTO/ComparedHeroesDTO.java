package br.com.gubee.interview.infrastructure.features.DTO;


import br.com.gubee.interview.domain.model.Comparison;

public record ComparedHeroesDTO(
        HeroToRequestDTO heroOne,
        HeroToRequestDTO heroTwo,
        Comparison comparison
) {
}
