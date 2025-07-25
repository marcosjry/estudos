package br.com.gubee.interview.application.port.in;

import br.com.gubee.interview.domain.model.Hero;

import java.util.List;
import java.util.UUID;

public interface FindHeroUseCase {

    List<Hero> searchByHeroName(String name);
    Hero findById(UUID id);

}
