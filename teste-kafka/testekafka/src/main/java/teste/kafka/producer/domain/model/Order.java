package teste.kafka.producer.domain.model;

import lombok.Data;
import teste.kafka.producer.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Order {

    private String customerId;
    private String id;
    private List<OrderItem> items;
    private Instant createdAt;
    private Instant updatedAt;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;

    private int attempt;

    public Order() {}

    private Order(List<OrderItem> items) {
        this.id = UUID.randomUUID().toString();
        this.customerId = UUID.randomUUID().toString();
        this.items = new ArrayList<>(items);
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.orderStatus = OrderStatus.PENDING;
        this.calculateTotal();
    }

    public static Order create(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item.");
        }
        return new Order(items);
    }

    public void confirm() {
        if (this.orderStatus != OrderStatus.PENDING) {
            throw new IllegalStateException("Only orders in PENDING can be confirmed.");
        }
        this.orderStatus = OrderStatus.CONFIRMED;
        this.touch();
    }

    private void calculateTotal() {
        this.totalAmount = this.items.stream()
                .map(OrderItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void cancel() {
        if (this.orderStatus == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel an order that has already been delivered.");
        }
        this.orderStatus = OrderStatus.CANCELLED;
        this.touch();
    }

    public void deliver() {
        if (this.orderStatus != OrderStatus.PENDING) {
            throw new IllegalStateException("Only orders in PENDING can be delivered.");
        }
        this.orderStatus = OrderStatus.DELIVERED;
        this.touch();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }


    @Override
    public String toString() {
        return "Order{" +
                "customerId='" + customerId + '\'' +
                ", id='" + id + '\'' +
                ", items=" + items +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", orderStatus=" + orderStatus +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
