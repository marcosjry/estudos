package com.gubee.poc.infrastructure.web.adapter;

import com.gubee.poc.domain.model.Order;
import com.gubee.poc.domain.model.OrderItem;
import com.gubee.poc.infrastructure.web.dto.OrderDTO;
import com.gubee.poc.infrastructure.web.dto.OrderItemDTO;
import jakarta.enterprise.context.ApplicationScoped;

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
