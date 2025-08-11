package com.gubee.poc.infrastructure.web.dto;

import java.util.List;

public record OrderDTO(
        List<OrderItemDTO> items
) {
}

