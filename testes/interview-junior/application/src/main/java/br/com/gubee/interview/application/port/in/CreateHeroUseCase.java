package br.com.gubee.interview.application.port.in;

import br.com.gubee.interview.domain.model.CommandHero;

import java.util.UUID;

public interface CreateHeroUseCase {
    UUID createHero(CommandHero hero);
}
