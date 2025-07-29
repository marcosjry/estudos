package br.com.gubee.interview.adapters.persistence;

import br.com.gubee.interview.adapters.DTO.HeroToUpdateDTO;
import br.com.gubee.interview.application.port.in.CommandHero;
import br.com.gubee.interview.adapters.DTO.ComparedHeroesDTO;
import br.com.gubee.interview.adapters.DTO.CreateHeroRequest;
import br.com.gubee.interview.adapters.DTO.HeroToRequestDTO;
import br.com.gubee.interview.application.port.in.ComparisonResult;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HeroMapper {

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

    public CommandHero toCommand(HeroToUpdateDTO dto) {
        if (dto == null) {
            return null;
        }

        return new CommandHero(
                dto.name(),
                dto.race(),
                dto.strength(),
                dto.agility(),
                dto.dexterity(),
                dto.intelligence()
        );
    }

    public HeroToRequestDTO toDto(Hero hero) {
        if (hero == null) {
            return null;
        }

        return new HeroToRequestDTO(
                hero.getId(),
                hero.getName(),
                hero.getRace(),
                hero.getPowerStats().getAgility(),
                hero.getPowerStats().getDexterity(),
                hero.getPowerStats().getStrength(),
                hero.getPowerStats().getIntelligence()
        );
    }

    public List<HeroToRequestDTO> toDto(List<Hero> heroes) {
        if (heroes == null) {
            return null;
        }

        return heroes.stream()
                .map(this::toDto)
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
                comparisonResult.comparison(),
                comparisonResult.winnerHeroName()
        );
    }

    public HeroJdbcEntity toJdbcEntity(Hero hero) {
        if (hero == null) {
            return null;
        }

        return new HeroJdbcEntity(
                hero.getId(),
                hero.getName(),
                hero.getRace(),
                hero.getPowerStats().getId(),
                hero.getCreatedAt(),
                hero.getUpdatedAt(),
                hero.isEnabled()
        );
    }

    public Hero toDomain(HeroJdbcEntity heroJdbcEntity, PowerStats powerStats) {
        if (heroJdbcEntity == null) {
            return null;
        }

        return new Hero(
                heroJdbcEntity.getId(),
                heroJdbcEntity.getName(),
                heroJdbcEntity.getRace(),
                powerStats,
                heroJdbcEntity.getCreatedAt(),
                heroJdbcEntity.getUpdatedAt(),
                heroJdbcEntity.isEnabled()
        );
    }


}
