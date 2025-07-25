package br.com.gubee.interview.application.service;

import br.com.gubee.interview.application.port.in.FindHeroUseCase;
import br.com.gubee.interview.application.port.out.HeroQueryPort;
import br.com.gubee.interview.domain.exception.NotFoundException;
import br.com.gubee.interview.domain.model.Hero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindHeroService implements FindHeroUseCase {

    private final HeroQueryPort heroQueryPort;

    @Override
    public List<Hero> searchByHeroName(String name) {
        return heroQueryPort.searchHeroByName(name);
    }

    @Override
    public Hero findById(UUID id) {
        return heroQueryPort.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Hero not found with id: " + id)
                );
    }
}
