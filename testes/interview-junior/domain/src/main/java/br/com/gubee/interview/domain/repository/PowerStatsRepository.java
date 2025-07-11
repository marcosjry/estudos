package br.com.gubee.interview.domain.repository;

import br.com.gubee.interview.domain.model.PowerStats;

import java.util.Optional;
import java.util.UUID;

public interface PowerStatsRepository {
    UUID create(PowerStats powerStats);
    Optional<PowerStats> findById(UUID id);
    void deleteById(UUID id);
    int update(PowerStats powerStats);
}
