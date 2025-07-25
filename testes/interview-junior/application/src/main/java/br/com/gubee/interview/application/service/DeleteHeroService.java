package br.com.gubee.interview.application.service;

import br.com.gubee.interview.application.port.in.DeleteHeroUseCase;
import br.com.gubee.interview.application.port.out.HeroCommandPort;
import br.com.gubee.interview.application.port.out.HeroQueryPort;
import br.com.gubee.interview.application.port.out.PowerStatsCommandPort;
import br.com.gubee.interview.application.port.out.PowerStatsQueryPort;
import br.com.gubee.interview.domain.exception.NotFoundException;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteHeroService implements DeleteHeroUseCase {

    private final PowerStatsCommandPort powerStatsCommandPort;
    private final HeroCommandPort heroCommandPort;

    private final PowerStatsQueryPort powerStatsQueryPort;
    private final HeroQueryPort heroQueryPort;

    @Override
    public void deleteHeroById(UUID id) throws NotFoundException {

        Hero hero = heroQueryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Hero not found with id: " + id));
        PowerStats powerStats = powerStatsQueryPort.findById(hero.getPowerStatsId())
                .orElseThrow(() -> new NotFoundException("PowerStats not found for with id: " + hero.getPowerStatsId())
        );

        heroCommandPort.deleteHeroById(hero.getId());
        powerStatsCommandPort.deleteById(powerStats.getId());
    }
}
