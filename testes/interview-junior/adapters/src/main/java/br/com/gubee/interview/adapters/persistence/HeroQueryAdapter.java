package br.com.gubee.interview.adapters.persistence;

import br.com.gubee.interview.application.port.out.HeroQueryPort;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
public class HeroQueryAdapter implements HeroQueryPort {

    private final PowerStatsQueryAdapter powerStatsQueryAdapter;
    private final HeroMapper heroMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String FIND_HERO_BY_ID_QUERY = "SELECT * from hero " +
            " WHERE id = :id";

    private static final String SEARCH_HERO_BY_NAME = "SELECT * from hero " +
            " WHERE name ILIKE :name";

    @Override
    public Optional<Hero> findById(UUID id) {
        Optional<HeroJdbcEntity> heroEntityOptional = findHeroEntityById(id);

        if (heroEntityOptional.isEmpty()) {
            return Optional.empty();
        }

        HeroJdbcEntity heroEntity = heroEntityOptional.get();
        PowerStats powerStats = this.powerStatsQueryAdapter.findById(heroEntity.getPowerStatsId())
                .orElseThrow(() -> new IllegalStateException("PowerStats not found for Hero with id: " + id)
        );

        Hero hero = heroMapper.toDomain(heroEntity, powerStats);
        return Optional.of(hero);
    }

    private Optional<HeroJdbcEntity> findHeroEntityById(UUID id) {
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        List<HeroJdbcEntity> results = namedParameterJdbcTemplate.query(
                FIND_HERO_BY_ID_QUERY,
                parameters,
                new BeanPropertyRowMapper<>(HeroJdbcEntity.class) // Mapper correto para a entidade!
        );
        return results.stream().findFirst();
    }


    @Override
    public List<Hero> searchHeroByName(String name) {
        String searchPattern = "%" + name + "%";
        SqlParameterSource parameters = new MapSqlParameterSource("name", searchPattern);
        List<HeroJdbcEntity> heroJdbcEntities = namedParameterJdbcTemplate.query(
                SEARCH_HERO_BY_NAME,
                parameters,
                new BeanPropertyRowMapper<>(HeroJdbcEntity.class));

        return heroJdbcEntities
                .stream()
                .map(hero -> {
                    PowerStats powerStats = this.powerStatsQueryAdapter.findById(hero.getPowerStatsId())
                            .orElseThrow(() -> new IllegalStateException("PowerStats not found for Hero with id: " + hero.getId()));

                    return heroMapper.toDomain(hero, powerStats);
                })
                .toList();

    }
}
