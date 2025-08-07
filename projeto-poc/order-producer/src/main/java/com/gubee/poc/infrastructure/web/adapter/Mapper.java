package com.gubee.poc.infrastructure.web.adapter;

import com.gubee.poc.domain.model.Order;
import com.gubee.poc.infrastructure.web.dto.OrderDTO;


public interface Mapper {
    Order toDomain(OrderDTO orderDTO);
}
