package teste.kafka.producer.web;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import teste.kafka.producer.application.port.in.OrderCreateUseCase;
import teste.kafka.producer.domain.model.Order;
import teste.kafka.producer.domain.model.OrderItem;
import teste.kafka.producer.web.adapter.Mapper;
import teste.kafka.producer.web.dto.OrderDTO;
import teste.kafka.producer.web.util.Message;

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
