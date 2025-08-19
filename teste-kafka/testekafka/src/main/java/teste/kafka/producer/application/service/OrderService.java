package teste.kafka.producer.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import teste.kafka.producer.application.port.in.OrderCreateUseCase;
import teste.kafka.producer.application.port.out.OrderProducerPort;
import teste.kafka.producer.domain.model.Order;

@ApplicationScoped
public class OrderService implements OrderCreateUseCase {

    @Inject
    OrderProducerPort senderPort;

    @Override
    public void create(Order order, int typeError) {
        String error = String.valueOf(typeError);
        senderPort.send(order, error);
    }
}
