package teste.kafka.producer.web.util;

public record Message<T>( String message, T payload) {
}
