package br.com.gubee.interview.application.port.in;

import br.com.gubee.interview.domain.model.ComparisonResult;

import java.util.UUID;

public interface CompareTwoHeroUseCase {

    ComparisonResult compare(UUID heroOneId, UUID heroTwoId);

}
