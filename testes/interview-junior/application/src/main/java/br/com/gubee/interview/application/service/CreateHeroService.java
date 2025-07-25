package br.com.gubee.interview.application.service;

import br.com.gubee.interview.application.port.in.CreateHeroUseCase;
import br.com.gubee.interview.domain.model.CommandHero;
import br.com.gubee.interview.application.port.out.HeroCommandPort;
import br.com.gubee.interview.application.port.out.PowerStatsCommandPort;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateHeroService implements CreateHeroUseCase {

    private final HeroCommandPort heroCommandPort;
    private final PowerStatsCommandPort powerStatsCommandPort;

    @Override
    public UUID createHero(CommandHero hero) {

        PowerStats powerStatsToCreate = PowerStats.builder()
                .strength(hero.strength())
                .agility(hero.agility())
                .dexterity(hero.dexterity())
                .intelligence(hero.intelligence())
                .build();

        UUID powerStatsId = powerStatsCommandPort.create(powerStatsToCreate);

        Hero heroToCreate = Hero.builder()
                .name(hero.name())
                .powerStatsId(powerStatsId)
                .race(hero.race())
                .createdAt(Instant.now())
                .build();

        return heroCommandPort.create(heroToCreate);
    }
}
