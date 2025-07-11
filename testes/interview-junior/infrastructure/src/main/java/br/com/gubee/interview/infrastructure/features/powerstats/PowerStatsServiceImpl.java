package br.com.gubee.interview.infrastructure.features.powerstats;

import br.com.gubee.interview.domain.model.PowerStats;
import br.com.gubee.interview.domain.dtos.HeroToUpdateDTO;
import br.com.gubee.interview.domain.exception.NotFoundException;
import br.com.gubee.interview.domain.service.PowerStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PowerStatsServiceImpl implements PowerStatsService {

    private final PowerStatsRepositoryImpl powerStatsRepositoryImpl;

    @Override
    @Transactional
    public UUID create(PowerStats powerStats) {
        return powerStatsRepositoryImpl.create(powerStats);
    }

    @Override
    public PowerStats findById(UUID id) {
        return powerStatsRepositoryImpl.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("PowerStats not found with id: " + id)
                );
    }

    @Override
    @Transactional
    public void deletePowerStatsById(UUID id) {
        powerStatsRepositoryImpl.deleteById(id);
    }

    @Override
    @Transactional
    public void updatePowerStats(PowerStats powerStats, HeroToUpdateDTO heroToUpdateDTO) {

        if(heroToUpdateDTO.agility() != null) {
            powerStats.setAgility(heroToUpdateDTO.agility());
        }

        if(heroToUpdateDTO.dexterity() != null) {
            powerStats.setDexterity(heroToUpdateDTO.dexterity());
        }

        if(heroToUpdateDTO.strength() != null) {
            powerStats.setStrength(heroToUpdateDTO.strength());
        }

        if(heroToUpdateDTO.intelligence() != null) {
            powerStats.setIntelligence(heroToUpdateDTO.intelligence());
        }

        powerStatsRepositoryImpl.update(powerStats);
    }
}
