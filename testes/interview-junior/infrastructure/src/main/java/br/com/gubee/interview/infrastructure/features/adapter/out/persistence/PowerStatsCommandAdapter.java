package br.com.gubee.interview.infrastructure.features.adapter.out.persistence;

import br.com.gubee.interview.application.port.out.PowerStatsCommandPort;
import br.com.gubee.interview.domain.model.PowerStats;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PowerStatsCommandAdapter implements PowerStatsCommandPort {

    private static final String CREATE_POWER_STATS_QUERY = "INSERT INTO power_stats" +
            " (strength, agility, dexterity, intelligence)" +
            " VALUES (:strength, :agility, :dexterity, :intelligence) RETURNING id";

    private static final String DELETE_POWER_STATS_BY_ID = "DELETE from power_stats " +
            " WHERE id = :id";

    private static final String UPDATE_POWER_STATS_QUERY = "UPDATE power_stats SET " +
            " strength = :strength," +
            " agility = :agility," +
            " dexterity = :dexterity," +
            " intelligence = :intelligence," +
            " updated_at = now()" +
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
    public void deleteById(UUID id) {
        SqlParameterSource parameter = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(
                DELETE_POWER_STATS_BY_ID,
                parameter
        );
    }

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
