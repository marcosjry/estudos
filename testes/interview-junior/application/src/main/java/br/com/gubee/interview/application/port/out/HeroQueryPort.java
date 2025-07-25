package br.com.gubee.interview.application.port.out;

import br.com.gubee.interview.domain.model.Hero;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HeroQueryPort {

    Optional<Hero> findById(UUID id);
    List<Hero> searchHeroByName(String name);

}
