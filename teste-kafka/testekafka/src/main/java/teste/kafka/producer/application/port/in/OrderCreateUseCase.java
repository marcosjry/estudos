package teste.kafka.producer.application.port.in;

import teste.kafka.producer.domain.model.Order;

public interface OrderCreateUseCase {
   void create(Order order, int typeError);
}
