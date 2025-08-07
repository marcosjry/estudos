package com.gubee.poc.infrastructure.web.dto;

public record OrderDTO(
        String id,
        String product,
        int quantity
) {
}
