package com.gubee.poc.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItem {

    private final UUID id;
    private final UUID productId;
    private final Integer quantity;
    private final BigDecimal price; // O preço no momento da compra

    public OrderItem(Integer quantity, BigDecimal price) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        this.id = UUID.randomUUID();
        this.productId = UUID.randomUUID();
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal getTotal() {
        return price.multiply(new BigDecimal(quantity));
    }

}
