package com.gubee.poc.infrastructure.web;

import com.gubee.poc.application.port.in.OrderCreateUseCase;
import com.gubee.poc.domain.model.Order;
import com.gubee.poc.infrastructure.web.adapter.Mapper;
import com.gubee.poc.infrastructure.web.dto.OrderDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {

    @Inject
    OrderCreateUseCase orderCreateUseCase;

    @Inject
    Mapper mapper;

    @POST
    public Response create(OrderDTO orderDto) {

        Order order = mapper.toDomain(orderDto);

        this.orderCreateUseCase.create(order);
        return Response.accepted(Response.Status.CREATED).build();
    }
}
