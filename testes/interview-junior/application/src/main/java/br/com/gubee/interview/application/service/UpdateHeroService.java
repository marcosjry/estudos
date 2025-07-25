package br.com.gubee.interview.application.service;

import br.com.gubee.interview.domain.model.CommandHero;
import br.com.gubee.interview.application.port.in.UpdateHeroUseCase;
import br.com.gubee.interview.application.port.out.HeroCommandPort;
import br.com.gubee.interview.application.port.out.HeroQueryPort;
import br.com.gubee.interview.application.port.out.PowerStatsCommandPort;
import br.com.gubee.interview.application.port.out.PowerStatsQueryPort;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateHeroService implements UpdateHeroUseCase {

    private final HeroCommandPort heroCommandPort;
    private final PowerStatsCommandPort powerStatsCommandPort;

    private final HeroQueryPort heroQueryPort;
    private final PowerStatsQueryPort powerStatsQueryPort;

    @Override
    public void updateHeroById(UUID id, CommandHero heroToUpdate) {

        Hero hero = heroQueryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hero not found with id: " + id));

        PowerStats powerStatsToUpdate = powerStatsQueryPort.findById(hero.getPowerStatsId())
                .orElseThrow(() -> new IllegalArgumentException("PowerStats not found with id: " + hero.getPowerStatsId()));

        updatePowerStats(powerStatsToUpdate, heroToUpdate);

        if(heroToUpdate.name() != null) {
            hero.setName(heroToUpdate.name());
        }
        if(heroToUpdate.race() != null) {
            hero.setRace(heroToUpdate.race());
        }

        heroCommandPort.update(hero);
    }

    public void updatePowerStats(PowerStats powerStats, CommandHero heroToUpdateDTO) {

        if(heroToUpdateDTO.agility() != null) {
            powerStats.setAgility(heroToUpdateDTO.agility());
        }

        if(heroToUpdateDTO.dexterity() != null) {
            powerStats.setDexterity(heroToUpdateDTO.dexterity());
        }

        if(heroToUpdateDTO.strength() != null) {
            powerStats.setStrength(heroToUpdateDTO.strength());
        }

        if(heroToUpdateDTO.intelligence() != null) {
            powerStats.setIntelligence(heroToUpdateDTO.intelligence());
        }

        powerStatsCommandPort.update(powerStats);
    }
}
