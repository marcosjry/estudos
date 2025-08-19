package teste.kafka.producer.web.adapter;



import teste.kafka.producer.domain.model.OrderItem;
import teste.kafka.producer.web.dto.OrderItemDTO;

import java.util.List;


public interface Mapper {

    OrderItem toDomain(OrderItemDTO orderItemDTO);

    List<OrderItem> toDomain(List<OrderItemDTO> orderItemsDTO);
}
