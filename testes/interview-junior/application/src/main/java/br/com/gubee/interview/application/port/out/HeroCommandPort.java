package br.com.gubee.interview.application.port.out;

import br.com.gubee.interview.domain.model.Hero;

import java.util.UUID;

public interface HeroCommandPort {

    UUID create(Hero hero);
    void deleteHero(Hero hero);
    int update(Hero hero);

}
