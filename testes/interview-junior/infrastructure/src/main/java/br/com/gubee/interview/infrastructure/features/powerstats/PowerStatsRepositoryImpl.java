package br.com.gubee.interview.infrastructure.features.powerstats;

import br.com.gubee.interview.domain.model.PowerStats;
import br.com.gubee.interview.domain.repository.PowerStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PowerStatsRepositoryImpl implements PowerStatsRepository {

    private static final String CREATE_POWER_STATS_QUERY = "INSERT INTO power_stats" +
        " (strength, agility, dexterity, intelligence)" +
        " VALUES (:strength, :agility, :dexterity, :intelligence) RETURNING id";

    private static final String FIND_POWER_STATS_BY_ID_QUERY = "SELECT * FROM power_stats WHERE id = :id";

    private static final String DELETE_POWER_STATS_BY_ID = "DELETE from power_stats " +
            " WHERE id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public UUID create(PowerStats powerStats) {
        return namedParameterJdbcTemplate.queryForObject(
            CREATE_POWER_STATS_QUERY,
            new BeanPropertySqlParameterSource(powerStats),
            UUID.class);
    }

    @Override
    public Optional<PowerStats> findById(UUID id) {
        SqlParameterSource parameter = new MapSqlParameterSource("id", id);
        List<PowerStats> results = namedParameterJdbcTemplate.query(
                FIND_POWER_STATS_BY_ID_QUERY,
                parameter,
                new BeanPropertyRowMapper<>(PowerStats.class));
        return results.stream().findFirst();
    }

    @Override
    public void deleteById(UUID id) {
        SqlParameterSource parameter = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(
                DELETE_POWER_STATS_BY_ID,
                parameter
        );
    }

    private static final String UPDATE_POWER_STATS_QUERY = "UPDATE power_stats SET " +
            " strength = :strength," +
            " agility = :agility," +
            " dexterity = :dexterity," +
            " intelligence = :intelligence," +
            " updated_at = now()" +
            " WHERE id = :id";

    @Override
    public int update(PowerStats powerStats) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("strength", powerStats.getStrength())
                .addValue("agility", powerStats.getAgility())
                .addValue("dexterity", powerStats.getDexterity())
                .addValue("intelligence", powerStats.getIntelligence())
                .addValue("id", powerStats.getId());

        return namedParameterJdbcTemplate.update(UPDATE_POWER_STATS_QUERY, parameters);
    }
}
