package com.gubee.poc.infrastructure.web;

import com.gubee.poc.application.port.in.OrderCreateUseCase;
import com.gubee.poc.domain.model.Order;
import com.gubee.poc.domain.model.OrderItem;
import com.gubee.poc.infrastructure.web.adapter.Mapper;
import com.gubee.poc.infrastructure.web.util.Message;
import com.gubee.poc.infrastructure.web.dto.OrderDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;


@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {

    @Inject
    OrderCreateUseCase orderCreateUseCase;

    @Inject
    Mapper mapper;

    @POST
    @Path("/{action}")
    public Response create(@PathParam("action") int action, OrderDTO orderDto) {
        try {
            List<OrderItem> items = mapper.toDomain(orderDto.items());
            Order order = Order.create(items);
            this.orderCreateUseCase.create(order, action);
            return Response.accepted().build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Message<>(e.getMessage(), orderDto)).build();
        }
    }
}
