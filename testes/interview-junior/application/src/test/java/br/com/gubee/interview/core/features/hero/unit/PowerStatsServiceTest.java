package br.com.gubee.interview.core.features.hero.unit;

import br.com.gubee.interview.domain.dtos.HeroToUpdateDTO;
import br.com.gubee.interview.domain.exception.NotFoundException;
import br.com.gubee.interview.domain.model.PowerStats;
import br.com.gubee.interview.infrastructure.features.powerstats.PowerStatsRepositoryImpl;
import br.com.gubee.interview.infrastructure.features.powerstats.PowerStatsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PowerStatsServiceTest {

    @Mock
    private PowerStatsRepositoryImpl powerStatsRepositoryImpl;

    @InjectMocks
    private PowerStatsServiceImpl powerStatsServiceImpl;

    @Test
    @DisplayName("Deve chamar o repositório para criar um PowerStats e retornar o UUID")
    void shouldCallRepositoryToCreateAndReturnId() {

        PowerStats powerStats = new PowerStats();
        UUID expectedId = UUID.randomUUID();
        when(powerStatsRepositoryImpl.create(powerStats)).thenReturn(expectedId);

        UUID actualId = powerStatsServiceImpl.create(powerStats);

        assertThat(actualId).isEqualTo(expectedId);
        verify(powerStatsRepositoryImpl, times(1)).create(powerStats);
    }

    @Test
    @DisplayName("Deve encontrar e retornar um PowerStats quando o ID existe")
    void shouldFindAndReturnPowerStatsWhenIdExists() {

        UUID id = UUID.randomUUID();
        PowerStats expectedPowerStats = PowerStats.builder().id(id).build();
        when(powerStatsRepositoryImpl.findById(id)).thenReturn(Optional.of(expectedPowerStats));

        PowerStats actualPowerStats = powerStatsServiceImpl.findById(id);

        assertThat(actualPowerStats).isEqualTo(expectedPowerStats);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando o ID não existe")
    void shouldThrowNotFoundExceptionWhenIdDoesNotExist() {

        UUID id = UUID.randomUUID();
        when(powerStatsRepositoryImpl.findById(id)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> powerStatsServiceImpl.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("PowerStats not found with id: " + id);
    }

    @Test
    @DisplayName("Deve chamar o repositório para deletar um PowerStats por ID")
    void shouldCallRepositoryToDeleteById() {

        UUID id = UUID.randomUUID();

        powerStatsServiceImpl.deletePowerStatsById(id);

        verify(powerStatsRepositoryImpl, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve atualizar todos os atributos de PowerStats quando o DTO está completo")
    void shouldUpdateAllAttributesWhenDtoIsComplete() {

        PowerStats existingPowerStats = PowerStats.builder()
                .strength(5).agility(5).dexterity(5).intelligence(5)
                .build();

        HeroToUpdateDTO updateDTO = new HeroToUpdateDTO(null, null, 10, 10, 10, 10);
        powerStatsServiceImpl.updatePowerStats(existingPowerStats, updateDTO);


        ArgumentCaptor<PowerStats> powerStatsCaptor = ArgumentCaptor.forClass(PowerStats.class);
        verify(powerStatsRepositoryImpl).update(powerStatsCaptor.capture());

        PowerStats updatedPowerStats = powerStatsCaptor.getValue();
        assertThat(updatedPowerStats.getStrength()).isEqualTo(10);
        assertThat(updatedPowerStats.getAgility()).isEqualTo(10);
        assertThat(updatedPowerStats.getDexterity()).isEqualTo(10);
        assertThat(updatedPowerStats.getIntelligence()).isEqualTo(10);
    }

    @Test
    @DisplayName("Deve atualizar apenas os atributos não nulos do DTO")
    void shouldUpdateOnlyNonNullAttributesFromDto() {

        PowerStats existingPowerStats = PowerStats.builder()
                .strength(5).agility(5).dexterity(5).intelligence(5)
                .build();

        HeroToUpdateDTO partialUpdateDTO = new HeroToUpdateDTO(null, null, null, null, 10, 10);

        powerStatsServiceImpl.updatePowerStats(existingPowerStats, partialUpdateDTO);

        ArgumentCaptor<PowerStats> powerStatsCaptor = ArgumentCaptor.forClass(PowerStats.class);
        verify(powerStatsRepositoryImpl).update(powerStatsCaptor.capture());

        PowerStats updatedPowerStats = powerStatsCaptor.getValue();

        assertThat(updatedPowerStats.getStrength()).isEqualTo(10);
        assertThat(updatedPowerStats.getIntelligence()).isEqualTo(10);

        assertThat(updatedPowerStats.getAgility()).isEqualTo(5);
        assertThat(updatedPowerStats.getDexterity()).isEqualTo(5);
    }
}
