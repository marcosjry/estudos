package com.gubee.poc.infrastructure.web.adapter;

import com.gubee.poc.domain.model.OrderItem;
import com.gubee.poc.infrastructure.web.dto.OrderItemDTO;

import java.util.List;


public interface Mapper {

    OrderItem toDomain(OrderItemDTO orderItemDTO);

    List<OrderItem> toDomain(List<OrderItemDTO> orderItemsDTO);
}
