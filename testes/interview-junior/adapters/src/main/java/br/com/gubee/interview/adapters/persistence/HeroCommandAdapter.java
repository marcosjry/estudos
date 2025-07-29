package br.com.gubee.interview.adapters.persistence;

import br.com.gubee.interview.application.port.out.HeroCommandPort;
import br.com.gubee.interview.application.port.out.PowerStatsCommandPort;
import br.com.gubee.interview.domain.exception.HeroNameAlreadyExistsException;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
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
    private final PowerStatsCommandAdapter powerStatsCommandAdapter;
    private final HeroMapper heroMapper;

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
        try {

            PowerStats powerStatsToSave = hero.getPowerStats();
            UUID persistedPowerStatsId = powerStatsCommandAdapter.create(powerStatsToSave);

            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("name", hero.getName())
                    .addValue("race", hero.getRace().name())
                    .addValue("powerStatsId", persistedPowerStatsId);

            return namedParameterJdbcTemplate.queryForObject(
                    CREATE_HERO_QUERY,
                    parameters,
                    UUID.class);
        } catch (DuplicateKeyException e) {
            throw new HeroNameAlreadyExistsException("Hero with name '" + hero.getName() + "' already exists.");
        }
    }

    @Transactional
    @Override
    public void deleteHero(Hero hero) {

        HeroJdbcEntity heroJdbcEntity = heroMapper.toJdbcEntity(hero);

        SqlParameterSource parameters = new MapSqlParameterSource("id", heroJdbcEntity.getId());
        namedParameterJdbcTemplate.update(
                DELETE_HERO_BY_ID,
                parameters
        );

        powerStatsCommandAdapter.deleteById(hero.getPowerStats().getId());

    }

    @Override
    public int update(Hero hero) {
        try {
            PowerStats powerStatsToUpdate = hero.getPowerStats();

            powerStatsCommandAdapter.update(powerStatsToUpdate);

            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("id", hero.getId())
                    .addValue("name", hero.getName())
                    .addValue("race", hero.getRace().name());

            return namedParameterJdbcTemplate.update(
                    UPDATE_HERO_QUERY,
                    parameters);
        } catch (DuplicateKeyException e) {
            throw new HeroNameAlreadyExistsException("Hero with name '" + hero.getName() + "' already exists.");
        }
    }
}
