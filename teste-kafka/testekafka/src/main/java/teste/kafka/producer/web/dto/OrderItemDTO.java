package teste.kafka.producer.web.dto;

import java.math.BigDecimal;

public record OrderItemDTO(
        Integer quantity,
        BigDecimal price) {
}
