package br.com.gubee.interview.application.service;

import br.com.gubee.interview.application.port.in.DeleteHeroUseCase;
import br.com.gubee.interview.application.port.out.HeroCommandPort;
import br.com.gubee.interview.application.port.out.HeroQueryPort;
import br.com.gubee.interview.application.port.out.PowerStatsCommandPort;
import br.com.gubee.interview.application.port.out.PowerStatsQueryPort;
import br.com.gubee.interview.domain.exception.NotFoundException;
import br.com.gubee.interview.domain.model.Hero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteHeroService implements DeleteHeroUseCase {

    private final HeroCommandPort heroCommandPort;
    private final HeroQueryPort heroQueryPort;

    @Override
    public void deleteHeroById(UUID id) throws NotFoundException {

        Hero hero = heroQueryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Hero not found with id: " + id));

        heroCommandPort.deleteHero(hero);
    }
}
