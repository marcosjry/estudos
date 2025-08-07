package com.gubee.poc.application.port.out;

import com.gubee.poc.domain.model.Order;

public interface OrderSenderPort {
    void send(Order order);
}
