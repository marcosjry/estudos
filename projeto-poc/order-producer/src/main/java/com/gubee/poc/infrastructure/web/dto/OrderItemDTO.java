package com.gubee.poc.infrastructure.web.dto;

import java.math.BigDecimal;

public record OrderItemDTO(
        Integer quantity,
        BigDecimal price) {
}
