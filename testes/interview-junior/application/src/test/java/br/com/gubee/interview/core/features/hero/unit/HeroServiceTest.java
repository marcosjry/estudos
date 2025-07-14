package br.com.gubee.interview.core.features.hero.unit;

import br.com.gubee.interview.domain.dtos.HeroToUpdateDTO;
import br.com.gubee.interview.domain.exception.NotFoundException;
import br.com.gubee.interview.domain.model.Hero;
import br.com.gubee.interview.domain.model.PowerStats;
import br.com.gubee.interview.domain.enums.Race;
import br.com.gubee.interview.domain.repository.HeroRepository;
import br.com.gubee.interview.domain.dtos.CreateHeroRequest;
import br.com.gubee.interview.domain.service.PowerStatsService;
import br.com.gubee.interview.infrastructure.features.hero.HeroServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HeroServiceTest {

    @Mock
    private HeroRepository heroRepositoryImpl;

    @Mock
    private PowerStatsService powerStatsServiceImpl;

    @InjectMocks
    private HeroServiceImpl heroServiceImpl;

    @Test
    @DisplayName("Deve criar um novo herói e seus atributos na ordem correta")
    void shouldCreateNewHeroAndStatsInCorrectOrder() {

        CreateHeroRequest request = CreateHeroRequest.builder()
                .name("Aquaman")
                .race(Race.DIVINE)
                .strength(9)
                .agility(7)
                .intelligence(6)
                .dexterity(8)
                .build();

        UUID generatedPowerStatsId = UUID.randomUUID();

        UUID generatedHeroId = UUID.randomUUID();

        when(powerStatsServiceImpl.create(any(PowerStats.class))).thenReturn(generatedPowerStatsId);
        when(heroRepositoryImpl.create(any(Hero.class))).thenReturn(generatedHeroId);

        UUID resultHeroId = heroServiceImpl.createNewHero(request);

        assertThat(resultHeroId).isEqualTo(generatedHeroId);

        InOrder inOrder = inOrder(powerStatsServiceImpl, heroRepositoryImpl);
        inOrder.verify(powerStatsServiceImpl).create(any(PowerStats.class));
        inOrder.verify(heroRepositoryImpl).create(any(Hero.class));
    }

    @Test
    @DisplayName("findById deve retornar Hero quando encontrado")
    void findByIdShouldReturnHeroWhenFound() {

        UUID id = UUID.randomUUID();
        Hero expectedHero = Hero.builder().id(id).name("The Flash").build();
        when(heroRepositoryImpl.findById(id)).thenReturn(Optional.of(expectedHero));

        Hero actualHero = heroServiceImpl.findById(id);

        assertThat(actualHero).isEqualTo(expectedHero);
    }

    @Test
    @DisplayName("findById deve lançar NotFoundException quando não encontrado")
    void findByIdShouldThrowNotFoundExceptionWhenNotFound() {

        UUID id = UUID.randomUUID();
        when(heroRepositoryImpl.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> heroServiceImpl.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Hero not found with id: " + id);
    }

    @Test
    @DisplayName("deleteHeroById deve deletar Hero e PowerStats associado")
    void deleteHeroByIdShouldDeleteHeroAndAssociatedPowerStats() {

        UUID heroId = UUID.randomUUID();
        UUID powerStatsId = UUID.randomUUID();

        Hero hero = Hero.builder().id(heroId).powerStatsId(powerStatsId).build();
        PowerStats powerStats = PowerStats.builder().id(powerStatsId).build();

        when(heroRepositoryImpl.findById(heroId)).thenReturn(Optional.of(hero));
        when(powerStatsServiceImpl.findById(powerStatsId)).thenReturn(powerStats);

        heroServiceImpl.deleteHeroById(heroId);

        verify(heroRepositoryImpl, times(1)).deleteHeroById(heroId);
        verify(powerStatsServiceImpl, times(1)).deletePowerStatsById(powerStatsId);
    }

    @Test
    @DisplayName("updateHeroById deve atualizar os dados do Herói e dos PowerStats")
    void updateHeroByIdShouldUpdateHeroAndPowerStatsData() {

        UUID heroId = UUID.randomUUID();
        UUID powerStatsId = UUID.randomUUID();

        Hero existingHero = Hero.builder().id(heroId).name("Old Name").race(Race.HUMAN).powerStatsId(powerStatsId).build();
        PowerStats existingPowerStats = PowerStats.builder().id(powerStatsId).build();

        HeroToUpdateDTO updateDTO = new HeroToUpdateDTO("New Name", Race.CYBORG, 10, 10, 10, 10);

        when(heroRepositoryImpl.findById(heroId)).thenReturn(Optional.of(existingHero));
        when(powerStatsServiceImpl.findById(powerStatsId)).thenReturn(existingPowerStats);

        heroServiceImpl.updateHeroById(heroId, updateDTO);

        ArgumentCaptor<Hero> heroCaptor = ArgumentCaptor.forClass(Hero.class);
        verify(heroRepositoryImpl).update(heroCaptor.capture());

        Hero updatedHero = heroCaptor.getValue();
        assertThat(updatedHero.getName()).isEqualTo("New Name");
        assertThat(updatedHero.getRace()).isEqualTo(Race.CYBORG);

        verify(powerStatsServiceImpl).updatePowerStats(eq(existingPowerStats), eq(updateDTO));
    }
}
