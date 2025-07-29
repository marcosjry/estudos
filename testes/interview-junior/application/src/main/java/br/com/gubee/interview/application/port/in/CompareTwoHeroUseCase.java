package br.com.gubee.interview.application.port.in;

import java.util.UUID;

public interface CompareTwoHeroUseCase {

    ComparisonResult compare(UUID heroOneId, UUID heroTwoId);

}
