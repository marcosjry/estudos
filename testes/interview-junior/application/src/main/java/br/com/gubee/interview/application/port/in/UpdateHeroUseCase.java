package br.com.gubee.interview.application.port.in;

import java.util.UUID;

public interface UpdateHeroUseCase {
    void updateHeroById(UUID id, CommandHero heroToUpdate);
}
