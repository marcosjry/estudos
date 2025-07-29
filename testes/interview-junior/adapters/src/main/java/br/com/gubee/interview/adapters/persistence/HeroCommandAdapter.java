package br.com.gubee.interview.infrastructure.adapter.out.persistence;

import br.com.gubee.interview.application.port.out.HeroCommandPort;
import br.com.gubee.interview.domain.model.Hero;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HeroCommandAdapter implements HeroCommandPort {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String CREATE_HERO_QUERY = "INSERT INTO hero" +
            " (name, race, power_stats_id)" +
            " VALUES (:name, :race, :powerStatsId) RETURNING id";

    private static final String DELETE_HERO_BY_ID = "DELETE from hero " +
            " WHERE id = :id";

    private static final String UPDATE_HERO_QUERY = "UPDATE hero SET " +
            " name = :name," +
            " race = :race," +
            " updated_at = now()" +
            " WHERE id = :id";

    @Transactional
    @Override
    public UUID create(Hero hero) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", hero.getName())
                .addValue("race", hero.getRace().name())
                .addValue("powerStatsId", hero.getPowerStatsId());

        return namedParameterJdbcTemplate.queryForObject(
                CREATE_HERO_QUERY,
                parameters,
                UUID.class);
    }

    @Override
    public void deleteHeroById(UUID id) {
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(
                DELETE_HERO_BY_ID,
                parameters
        );
    }

    @Override
    public int update(Hero hero) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", hero.getId())
                .addValue("name", hero.getName())
                .addValue("race", hero.getRace().name());

        return namedParameterJdbcTemplate.update(UPDATE_HERO_QUERY, parameters);
    }
}
