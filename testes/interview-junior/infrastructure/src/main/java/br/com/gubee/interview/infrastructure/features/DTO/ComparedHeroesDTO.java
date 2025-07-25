package br.com.gubee.interview.domain.dtos;


import br.com.gubee.interview.domain.model.Comparison;

public record ComparedHeroesDTO(
        HeroToRequestDTO heroOne,
        HeroToRequestDTO heroTwo,
        Comparison comparison
) {
}
