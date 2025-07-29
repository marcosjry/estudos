package br.com.gubee.interview.domain.exception;

public class HeroNameAlreadyExistsException extends RuntimeException {

    public HeroNameAlreadyExistsException(String message) {
        super(message);
    }

    public HeroNameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
