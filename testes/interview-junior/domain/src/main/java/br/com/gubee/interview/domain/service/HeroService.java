package br.com.gubee.interview.domain.service;

import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.dtos.CompareHero;
import br.com.gubee.interview.domain.dtos.HeroToRequestDTO;
import br.com.gubee.interview.domain.dtos.HeroToUpdateDTO;
import br.com.gubee.interview.domain.exception.NotFoundException;
import br.com.gubee.interview.domain.dtos.CreateHeroRequest;


import java.util.List;
import java.util.UUID;

public interface HeroService {

    UUID createNewHero(CreateHeroRequest createHeroRequest);
    Hero findById(UUID id);
    HeroToRequestDTO findHeroByIdToRequestDTO(UUID id);
    List<HeroToRequestDTO> searchByHeroName(String name);
    void deleteHeroById(UUID id) throws NotFoundException;
    void updateHeroById(UUID id, HeroToUpdateDTO heroToUpdateDTO);
    CompareHero compareTwoHeroes(UUID heroIdOne, UUID heroIdTwo);
}
