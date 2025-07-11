package br.com.gubee.interview.domain.dtos;



public record CompareHero(
        HeroToRequestDTO heroOne,
        HeroToRequestDTO heroTwo,
        Comparison comparison
) {
}
