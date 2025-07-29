package br.com.gubee.interview.adapters.DTO;

import br.com.gubee.interview.domain.enums.Race;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateHeroRequest {

    @NotBlank(message = "message.name.mandatory")
    @Length(min = 1, max = 255, message = "message.name.length")
    private String name;

    @NotNull(message = "message.race.mandatory")
    private Race race;

    @Min(value = 0, message = "message.powerstats.strength.min")
    @Max(value = 10, message = "message.powerstats.strength.max")
    @NotNull(message = "message.powerstats.strength.mandatory")
    private Integer strength;

    @Min(value = 0, message = "message.powerstats.agility.min")
    @Max(value = 10, message = "message.powerstats.agility.max")
    @NotNull(message = "message.powerstats.agility.mandatory")
    private Integer agility;

    @Min(value = 0, message = "message.powerstats.dexterity.min")
    @Max(value = 10, message = "message.powerstats.dexterity.max")
    @NotNull(message = "message.powerstats.dexterity.mandatory")
    private Integer dexterity;

    @Min(value = 0, message = "message.powerstats.intelligence.min")
    @Max(value = 10, message = "message.powerstats.intelligence.max")
    @NotNull(message = "message.powerstats.intelligence.mandatory")
    private Integer intelligence;

}
