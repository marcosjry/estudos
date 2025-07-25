package br.com.gubee.interview.application.port.out;

import br.com.gubee.interview.domain.model.PowerStats;

import java.util.UUID;

public interface PowerStatsCommandPort {

    UUID create(PowerStats powerStats);
    void deleteById(UUID id);
    int update(PowerStats powerStats);

}
