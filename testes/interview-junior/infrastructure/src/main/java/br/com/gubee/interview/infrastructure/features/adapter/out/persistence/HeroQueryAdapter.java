package br.com.gubee.interview.infrastructure.features.adapter.out.persistence;

import br.com.gubee.interview.application.port.out.HeroQueryPort;
import br.com.gubee.interview.domain.model.Hero;
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
public class HeroQueryAdapter implements HeroQueryPort {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String FIND_HERO_BY_ID_QUERY = "SELECT * from hero " +
            " WHERE id = :id";

    private static final String SEARCH_HERO_BY_NAME = "SELECT * from hero " +
            " WHERE name ILIKE :name";

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
    public List<Hero> searchHeroByName(String name) {
        String searchPattern = "%" + name + "%";
        SqlParameterSource parameters = new MapSqlParameterSource("name", searchPattern);
        return namedParameterJdbcTemplate.query(
                SEARCH_HERO_BY_NAME,
                parameters,
                new BeanPropertyRowMapper<>(Hero.class));
    }
}
