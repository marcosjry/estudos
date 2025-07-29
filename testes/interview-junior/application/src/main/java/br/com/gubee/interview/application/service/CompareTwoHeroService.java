package br.com.gubee.interview.application.service;

import br.com.gubee.interview.application.port.in.CompareTwoHeroUseCase;
import br.com.gubee.interview.application.port.out.HeroQueryPort;
import br.com.gubee.interview.domain.model.Comparison;
import br.com.gubee.interview.application.port.in.ComparisonResult;
import br.com.gubee.interview.domain.model.Hero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompareTwoHeroService implements CompareTwoHeroUseCase {

    private final HeroQueryPort heroQueryPort;

    @Override
    public ComparisonResult compare(UUID heroOneId, UUID heroTwoId) {

        Hero heroOne = heroQueryPort.findById(heroOneId)
                .orElseThrow(() -> new IllegalArgumentException("Hero one not found"));
        Hero heroTwo = heroQueryPort.findById(heroTwoId)
                .orElseThrow(() -> new IllegalArgumentException("Hero two not found"));

        Comparison comparison = heroOne.compareStatsWith(heroTwo);

        String winnerHeroName =  heroOne.getPowerStats().getTotal() > heroTwo.getPowerStats().getTotal() ? heroOne.getName() : heroTwo.getName();

        if (heroTwo.getPowerStats().getTotal() == heroTwo.getPowerStats().getTotal())
            winnerHeroName = "DRAW";

        return new ComparisonResult(heroOne, heroTwo, comparison, winnerHeroName);
    }
}
