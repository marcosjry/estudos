package br.com.gubee.interview.application.service;

import br.com.gubee.interview.application.port.in.CompareTwoHeroUseCase;
import br.com.gubee.interview.application.port.out.HeroQueryPort;
import br.com.gubee.interview.application.port.out.PowerStatsQueryPort;
import br.com.gubee.interview.domain.model.Comparison;
import br.com.gubee.interview.domain.model.ComparisonResult;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompareTwoHeroService implements CompareTwoHeroUseCase {

    private final HeroQueryPort heroQueryPort;
    private final PowerStatsQueryPort powerStatsQueryPort;

    @Override
    public ComparisonResult compare(UUID heroOneId, UUID heroTwoId) {
        Hero heroOne = heroQueryPort.findById(heroOneId)
                .orElseThrow(() -> new IllegalArgumentException("Hero one not found"));
        Hero heroTwo = heroQueryPort.findById(heroTwoId)
                .orElseThrow(() -> new IllegalArgumentException("Hero two not found"));

        PowerStats powerStatsOne = powerStatsQueryPort.findById(heroOne.getPowerStatsId())
                .orElseThrow(() -> new IllegalArgumentException("PowerStats for hero one not found"));
        PowerStats powerStatsTwo = powerStatsQueryPort.findById(heroTwo.getPowerStatsId())
                .orElseThrow(() -> new IllegalArgumentException("PowerStats for hero two not found"));

        Comparison comparison = new Comparison(
                powerStatsOne.getAgility() - powerStatsTwo.getAgility(),
                powerStatsOne.getDexterity() - powerStatsTwo.getDexterity(),
                powerStatsOne.getStrength() - powerStatsTwo.getStrength(),
                powerStatsOne.getIntelligence() - powerStatsTwo.getIntelligence()
        );

        return new ComparisonResult(heroOne, heroTwo, comparison);
    }
}
