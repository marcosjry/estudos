package br.com.gubee.interview.infrastructure.features.adapter.in.web.mapper;

import br.com.gubee.interview.domain.model.CommandHero;
import br.com.gubee.interview.application.port.out.PowerStatsQueryPort;
import br.com.gubee.interview.infrastructure.features.DTO.ComparedHeroesDTO;
import br.com.gubee.interview.infrastructure.features.DTO.CreateHeroRequest;
import br.com.gubee.interview.infrastructure.features.DTO.HeroToRequestDTO;
import br.com.gubee.interview.domain.model.ComparisonResult;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HeroMapper {

    private final PowerStatsQueryPort powerStatsQueryPort;

    public CommandHero toCommand(CreateHeroRequest dto) {
        if (dto == null) {
            return null;
        }

        return new CommandHero(
                dto.getName(),
                dto.getRace(),
                dto.getStrength(),
                dto.getAgility(),
                dto.getDexterity(),
                dto.getIntelligence()
        );
    }

    public HeroToRequestDTO toDto(Hero hero) {
        if (hero == null) {
            return null;
        }

        PowerStats stats = powerStatsQueryPort.findById(hero.getPowerStatsId()).orElseThrow(
                () -> new IllegalArgumentException("PowerStats not found with ID: " + hero.getPowerStatsId())
        );

        return new HeroToRequestDTO(
                hero.getId(),
                hero.getName(),
                hero.getRace().name(),
                stats.getStrength(),
                stats.getAgility(),
                stats.getDexterity(),
                stats.getIntelligence()
        );
    }

    public List<HeroToRequestDTO> toDto(List<Hero> heroes) {
        if (heroes == null) {
            return null;
        }

        return heroes.stream()
                .map(hero -> {
                    PowerStats stats = powerStatsQueryPort.findById(hero.getPowerStatsId())
                            .orElseThrow(() -> new IllegalArgumentException("PowerStats not found with ID: " + hero.getPowerStatsId()));

                    return new HeroToRequestDTO(
                            hero.getId(),
                            hero.getName(),
                            hero.getRace().name(),
                            stats.getAgility(),
                            stats.getDexterity(),
                            stats.getStrength(),
                            stats.getIntelligence()
                    );
                })
                .toList();
    }

    public ComparedHeroesDTO toDto(ComparisonResult comparisonResult) {
        if (comparisonResult == null) {
            return null;
        }

        HeroToRequestDTO heroOneDto = toDto(comparisonResult.heroOne());
        HeroToRequestDTO heroTwoDto = toDto(comparisonResult.heroTwo());
        return new ComparedHeroesDTO(
                heroOneDto,
                heroTwoDto,
                comparisonResult.comparison()
        );
    }


}
