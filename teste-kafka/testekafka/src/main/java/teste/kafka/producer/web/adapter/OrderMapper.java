package teste.kafka.producer.web.adapter;


import jakarta.enterprise.context.ApplicationScoped;
import teste.kafka.producer.domain.model.OrderItem;
import teste.kafka.producer.web.dto.OrderItemDTO;

import java.util.List;


@ApplicationScoped
public class OrderMapper implements Mapper {


    @Override
    public OrderItem toDomain(OrderItemDTO orderItemDTO) {
        return new OrderItem(
                orderItemDTO.quantity(),
                orderItemDTO.price()
        );
    }

    @Override
    public List<OrderItem> toDomain(List<OrderItemDTO> orderItemsDTO) {
        return orderItemsDTO.stream()
                .map(this::toDomain)
                .toList();
    }


}
