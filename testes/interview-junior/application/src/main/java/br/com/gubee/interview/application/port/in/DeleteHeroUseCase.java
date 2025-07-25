package br.com.gubee.interview.application.port.in;

import br.com.gubee.interview.domain.exception.NotFoundException;

import java.util.UUID;

public interface DeleteHeroUseCase {
    void deleteHeroById(UUID id) throws NotFoundException;
}
