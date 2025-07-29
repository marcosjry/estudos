package br.com.gubee.interview.adapters.persistence;

import br.com.gubee.interview.domain.enums.Race;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeroJdbcEntity {

    private UUID id;
    private String name;
    private Race race;
    private UUID powerStatsId;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean enabled;

}
