package com.gubee.poc.application.port.in;

import com.gubee.poc.domain.model.Order;

public interface OrderCreateUseCase {
   void create(Order order);
}
