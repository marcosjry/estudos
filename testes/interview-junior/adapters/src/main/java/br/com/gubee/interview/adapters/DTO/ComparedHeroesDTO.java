package br.com.gubee.interview.adapters.DTO;


import br.com.gubee.interview.domain.model.Comparison;

public record ComparedHeroesDTO(
        HeroToRequestDTO heroOne,
        HeroToRequestDTO heroTwo,
        Comparison comparison,
        String winnerHeroName
) {
}
