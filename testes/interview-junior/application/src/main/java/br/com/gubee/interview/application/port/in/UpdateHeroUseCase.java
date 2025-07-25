package br.com.gubee.interview.application.port.in;

import br.com.gubee.interview.domain.model.CommandHero;

import java.util.UUID;

public interface UpdateHeroUseCase {
    void updateHeroById(UUID id, CommandHero heroToUpdate);
}
