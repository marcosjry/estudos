package br.com.gubee.interview.application.port.in;

import br.com.gubee.interview.domain.model.Comparison;
import br.com.gubee.interview.domain.model.Hero;

public record ComparisonResult(
        Hero heroOne,
        Hero heroTwo,
        Comparison comparison,
        String winnerHeroName
) {
}
