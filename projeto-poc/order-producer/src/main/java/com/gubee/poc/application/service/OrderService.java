package com.gubee.poc.application.service;

import com.gubee.poc.application.port.in.OrderCreateUseCase;
import com.gubee.poc.domain.model.Order;
import com.gubee.poc.application.port.out.OrderSenderPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OrderService implements OrderCreateUseCase {

    @Inject
    OrderSenderPort senderPort;

    @Override
    public void create(Order order) {
        senderPort.send(order);
    }
}
