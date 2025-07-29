package br.com.gubee.interview.adapters.configuration;

import br.com.gubee.interview.application.port.in.*;
import br.com.gubee.interview.application.port.out.HeroCommandPort;
import br.com.gubee.interview.application.port.out.HeroQueryPort;
import br.com.gubee.interview.application.port.out.PowerStatsCommandPort;
import br.com.gubee.interview.application.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceAdapterConfiguration {

    @Bean
    public CreateHeroUseCase createHeroUseCase(
            HeroCommandPort heroCommandPort,
            PowerStatsCommandPort powerStatsCommandPort) {
        return new CreateHeroService(
                heroCommandPort,
                powerStatsCommandPort
        );
    }

    @Bean
    public FindHeroUseCase findHeroUseCase(
            HeroQueryPort heroCommandPort) {
        return new FindHeroService(heroCommandPort);
    }

    @Bean
    public DeleteHeroUseCase deleteHeroUseCase(
            HeroCommandPort heroCommandPort, HeroQueryPort heroQueryPort) {
        return new DeleteHeroService(heroCommandPort, heroQueryPort);
    }

    @Bean
    public UpdateHeroUseCase updateHeroUseCase(
            HeroCommandPort heroCommandPort, HeroQueryPort heroQueryPort) {
        return new UpdateHeroService(heroCommandPort, heroQueryPort);
    }

    @Bean
    public CompareTwoHeroUseCase compareTwoHeroUseCase(
            HeroQueryPort heroQueryPort) {
        return new CompareTwoHeroService(heroQueryPort);
    }

}
