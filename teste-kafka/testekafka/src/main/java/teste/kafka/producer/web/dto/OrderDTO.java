package teste.kafka.producer.web.dto;

import java.util.List;

public record OrderDTO(
        List<OrderItemDTO> items
) {
}

