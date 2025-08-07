package com.gubee.poc;

import com.gubee.poc.domain.Order;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class JmsOrderConsumer {

    @Inject
    Jsonb jsonb;

    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    @Incoming("orders-in")
    public void processOrder(String orderPayload) {
        new Thread(() -> {
            try {

                System.out.println("📥 Recebendo pedido: " + orderPayload);
                process(orderPayload);

            } catch (Exception e) {
                System.err.println("❌ Erro ao desserializar a mensagem: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    @Bulkhead(value = 3, waitingTaskQueue = 10)
    public void process(String payload) {
        Order order = jsonb.fromJson(payload, Order.class);
        System.out.printf(" Pedido recebido: ID=%s | Produto=%s | Quantidade=%d%n",
                order.getId(), order.getProduct(), order.getQuantity());
        simulateProcessing();
    }

    private void simulateProcessing() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}