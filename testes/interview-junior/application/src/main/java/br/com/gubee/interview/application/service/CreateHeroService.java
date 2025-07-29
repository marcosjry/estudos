package br.com.gubee.interview.application.service;

import br.com.gubee.interview.application.port.in.CreateHeroUseCase;
import br.com.gubee.interview.application.port.in.CommandHero;
import br.com.gubee.interview.application.port.out.HeroCommandPort;
import br.com.gubee.interview.application.port.out.PowerStatsCommandPort;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateHeroService implements CreateHeroUseCase {

    private final HeroCommandPort heroCommandPort;
    private final PowerStatsCommandPort powerStatsCommandPort;

    @Override
    public UUID createHero(CommandHero hero) {

        PowerStats powerStatsToCreate = new PowerStats(
                hero.strength(),
                hero.agility(),
                hero.dexterity(),
                hero.intelligence()
        );

        Hero heroToCreate = new Hero(
                hero.name(),
                hero.race(),
                powerStatsToCreate
        );

        return heroCommandPort.create(heroToCreate);
    }
}
