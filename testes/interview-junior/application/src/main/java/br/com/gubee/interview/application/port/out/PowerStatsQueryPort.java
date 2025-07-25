package br.com.gubee.interview.application.port.out;

import br.com.gubee.interview.domain.model.PowerStats;

import java.util.Optional;
import java.util.UUID;

public interface PowerStatsQueryPort {

    Optional<PowerStats> findById(UUID id);

}
