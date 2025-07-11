package br.com.gubee.interview.domain.repository;

import br.com.gubee.interview.domain.model.Hero;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HeroRepository {

    UUID create(Hero hero);
    Optional<Hero> findById(UUID id);
    List<Hero> searchByHeroName(String name);
    void deleteHeroById(UUID id);
    int update(Hero hero);
}
