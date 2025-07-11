package br.com.gubee.interview.infrastructure.features.hero;

import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.repository.HeroRepository;
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
public class HeroRepositoryImpl implements HeroRepository {

    private static final String CREATE_HERO_QUERY = "INSERT INTO hero" +
            " (name, race, power_stats_id)" +
            " VALUES (:name, :race, :powerStatsId) RETURNING id";

    private static final String FIND_HERO_BY_ID_QUERY = "SELECT * from hero " +
            " WHERE id = :id";

    private static final String SEARCH_HERO_BY_NAME = "SELECT * from hero " +
            " WHERE name ILIKE :name";

    private static final String DELETE_HERO_BY_ID = "DELETE from hero " +
            " WHERE id = :id";

    private static final String UPDATE_HERO_QUERY = "UPDATE hero SET " +
            " name = :name," +
            " race = :race," +
            " updated_at = now()" +
            " WHERE id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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
    public Optional<Hero> findById(UUID id) {
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        List<Hero> results = namedParameterJdbcTemplate.query(
                FIND_HERO_BY_ID_QUERY,
                parameters,
                new BeanPropertyRowMapper<>(Hero.class));
        return results.stream().findFirst();
    }

    @Override
    public List<Hero> searchByHeroName(String name) {
        String searchPattern = "%" + name + "%";
        SqlParameterSource parameters = new MapSqlParameterSource("name", searchPattern);
        return namedParameterJdbcTemplate.query(
                SEARCH_HERO_BY_NAME,
                parameters,
                new BeanPropertyRowMapper<>(Hero.class));
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
