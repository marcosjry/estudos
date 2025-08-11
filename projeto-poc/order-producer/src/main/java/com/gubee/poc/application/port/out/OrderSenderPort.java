package com.gubee.poc.application.port.out;

import com.gubee.poc.domain.model.Order;

public interface OrderSenderPort {
    Order send(Order order, String typeError);
    Order fallbackSend(Order order, String typeError);
}
