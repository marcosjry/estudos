package br.com.gubee.interview.application.service;

import br.com.gubee.interview.application.port.in.CommandHero;
import br.com.gubee.interview.application.port.in.UpdateHeroUseCase;
import br.com.gubee.interview.application.port.out.HeroCommandPort;
import br.com.gubee.interview.application.port.out.HeroQueryPort;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateHeroService implements UpdateHeroUseCase {

    private final HeroCommandPort heroCommandPort;
    private final HeroQueryPort heroQueryPort;

    @Override
    public void updateHeroById(UUID id, CommandHero heroToUpdate) {

        Hero hero = heroQueryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hero not found with id: " + id));

        PowerStats powerStatsToUpdate = new PowerStats(
                heroToUpdate.strength() != null ? heroToUpdate.strength() : -1,
                heroToUpdate.agility() != null ? heroToUpdate.agility() : -1,
                heroToUpdate.dexterity() != null ? heroToUpdate.dexterity() : -1,
                heroToUpdate.intelligence() != null ? heroToUpdate.intelligence() : -1
        );

        hero.update(
                heroToUpdate.name(),
                heroToUpdate.race(),
                powerStatsToUpdate
        );

        heroCommandPort.update(hero);
    }

}
