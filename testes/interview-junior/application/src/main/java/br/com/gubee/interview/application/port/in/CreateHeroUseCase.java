package br.com.gubee.interview.application.port.in;

import java.util.UUID;

public interface CreateHeroUseCase {
    UUID createHero(CommandHero hero);
}
