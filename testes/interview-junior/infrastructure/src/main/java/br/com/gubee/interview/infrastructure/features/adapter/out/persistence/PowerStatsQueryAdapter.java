package br.com.gubee.interview.infrastructure.features.adapter.out.persistence;

import br.com.gubee.interview.application.port.out.PowerStatsQueryPort;
import br.com.gubee.interview.domain.model.PowerStats;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PowerStatsQueryAdapter implements PowerStatsQueryPort {

    private static final String FIND_POWER_STATS_BY_ID_QUERY = "SELECT * FROM power_stats WHERE id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<PowerStats> findById(UUID id) {
        SqlParameterSource parameter = new MapSqlParameterSource("id", id);
        List<PowerStats> results = namedParameterJdbcTemplate.query(
                FIND_POWER_STATS_BY_ID_QUERY,
                parameter,
                new BeanPropertyRowMapper<>(PowerStats.class));
        return results.stream().findFirst();
    }
}
