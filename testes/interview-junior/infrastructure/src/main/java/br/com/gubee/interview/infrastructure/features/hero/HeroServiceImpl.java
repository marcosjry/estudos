package br.com.gubee.interview.infrastructure.features.hero;

import br.com.gubee.interview.domain.dtos.CompareHero;
import br.com.gubee.interview.domain.dtos.Comparison;
import br.com.gubee.interview.domain.dtos.HeroToUpdateDTO;
import br.com.gubee.interview.domain.exception.NotFoundException;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import br.com.gubee.interview.domain.dtos.HeroToRequestDTO;
import br.com.gubee.interview.domain.repository.HeroRepository;
import br.com.gubee.interview.domain.dtos.CreateHeroRequest;
import br.com.gubee.interview.domain.service.HeroService;
import br.com.gubee.interview.domain.service.PowerStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class HeroServiceImpl implements HeroService {

    private final HeroRepository heroRepositoryImpl;

    private final PowerStatsService powerStatsServiceImpl;

    @Override
    @Transactional
    public UUID createNewHero(CreateHeroRequest createHeroRequest) {

        PowerStats powerStatsToCreate = new PowerStats(createHeroRequest);

        UUID powerStatsId = powerStatsServiceImpl.create(powerStatsToCreate);

        Hero heroToCreate = new Hero(createHeroRequest, powerStatsId);

        return heroRepositoryImpl.create(heroToCreate);
    }

    @Override
    public Hero findById(UUID id) {
        return heroRepositoryImpl.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Hero not found with id: " + id)
                );
    }

    @Override
    public HeroToRequestDTO findHeroByIdToRequestDTO(UUID id) {
        Hero hero = findById(id);
        PowerStats powerStats = powerStatsServiceImpl.findById(hero.getPowerStatsId());
        return new HeroToRequestDTO(
                hero.getId(),
                hero.getName(),
                hero.getRace().name(),
                powerStats.getAgility(),
                powerStats.getDexterity(),
                powerStats.getStrength(),
                powerStats.getIntelligence()
        );
    }

    @Override
    public List<HeroToRequestDTO> searchByHeroName(String name) {

            List<Hero> heroes = heroRepositoryImpl.searchByHeroName(name);
            return heroes.stream()
                    .map(hero -> {
                        PowerStats powerStats = powerStatsServiceImpl.findById(hero.getPowerStatsId());
                        return new HeroToRequestDTO(
                                hero.getId(),
                                hero.getName(),
                                hero.getRace().name(),
                                powerStats.getAgility(),
                                powerStats.getDexterity(),
                                powerStats.getStrength(),
                                powerStats.getIntelligence()
                        );
                    })
                    .toList();
    }

    @Override
    @Transactional
    public void deleteHeroById(UUID id) {
        Hero hero = findById(id);
        PowerStats powerStats = powerStatsServiceImpl.findById(hero.getPowerStatsId());
        heroRepositoryImpl.deleteHeroById(id);
        powerStatsServiceImpl.deletePowerStatsById(powerStats.getId());
    }

    @Override
    @Transactional
    public void updateHeroById(UUID id, HeroToUpdateDTO heroToUpdateDTO) {
        Hero hero = findById(id);
        PowerStats powerStatsToUpdate = powerStatsServiceImpl.findById(hero.getPowerStatsId());
        powerStatsServiceImpl.updatePowerStats(powerStatsToUpdate, heroToUpdateDTO);

        if(heroToUpdateDTO.name() != null) {
            hero.setName(heroToUpdateDTO.name());
        }
        if(heroToUpdateDTO.race() != null) {
            hero.setRace(heroToUpdateDTO.race());
        }

        heroRepositoryImpl.update(hero);
    }

    @Override
    public CompareHero compareTwoHeroes(UUID heroIdOne, UUID heroIdTwo) {
        Hero heroOne = findById(heroIdOne);
        Hero heroTwo = findById(heroIdTwo);

        PowerStats powerStatsOne = powerStatsServiceImpl.findById(heroOne.getPowerStatsId());
        PowerStats powerStatsTwo = powerStatsServiceImpl.findById(heroTwo.getPowerStatsId());
        return new CompareHero(
                heroFormatter(heroOne, powerStatsOne),
                heroFormatter(heroTwo, powerStatsTwo),
                new Comparison(
                        powerStatsOne.getAgility() - powerStatsTwo.getAgility(),
                        powerStatsOne.getDexterity() - powerStatsTwo.getDexterity(),
                        powerStatsOne.getStrength() - powerStatsTwo.getStrength(),
                        powerStatsOne.getIntelligence() - powerStatsTwo.getIntelligence()
                )
        );
    }

    public HeroToRequestDTO heroFormatter(Hero hero, PowerStats powerStats) {
        return new HeroToRequestDTO(
                hero.getId(),
                hero.getName(),
                hero.getRace().name(),
                powerStats.getAgility(),
                powerStats.getDexterity(),
                powerStats.getStrength(),
                powerStats.getIntelligence()
        );
    }
}
