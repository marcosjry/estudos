package com.gubee.poc.infrastructure.web.adapter;

import com.gubee.poc.domain.model.Order;
import com.gubee.poc.infrastructure.web.dto.OrderDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderMapper implements Mapper {

    @Override
    public Order toDomain(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }

        Order order = new Order();
        order.setId(orderDTO.id());
        order.setProduct(orderDTO.product());
        order.setQuantity(orderDTO.quantity());

        return order;
    }
 }
