package br.com.gubee.interview.domain.service;

import br.com.gubee.interview.domain.model.PowerStats;
import br.com.gubee.interview.domain.dtos.HeroToUpdateDTO;

import java.util.UUID;

public interface PowerStatsService {
    UUID create(PowerStats powerStats);
    PowerStats findById(UUID id);
    void deletePowerStatsById(UUID id);
    void updatePowerStats(PowerStats powerStats, HeroToUpdateDTO heroToUpdateDTO);

}
