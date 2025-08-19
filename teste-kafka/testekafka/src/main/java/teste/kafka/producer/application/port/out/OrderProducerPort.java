package teste.kafka.producer.application.port.out;


import teste.kafka.producer.domain.model.Order;

public interface OrderProducerPort {
    Order send(Order order, String typeError);
    Order fallbackSend(Order order, String typeError);
}
