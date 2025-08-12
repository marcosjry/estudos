package com.gubee.poc;


import com.gubee.poc.domain.model.Order;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.json.bind.Jsonb;
import org.eclipse.microprofile.faulttolerance.*;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.Random;

@ApplicationScoped
public class JmsOrderConsumer {

    @Inject
    Jsonb jsonb;

    @Inject
    ConnectionFactory connectionFactory;

    @Incoming("orders-in")
    public void processOrder(String orderPayload) {
        System.out.println("📥 Pedido Recebido");
        process(orderPayload);
    }

    @Retry(maxRetries = 3, delay = 1000)
    @Timeout(5000)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    @Bulkhead(value = 3, waitingTaskQueue = 10)
    @Fallback(fallbackMethod = "fallbackProcess")
    public void process(String payload) {
        try {
            System.out.println("⚙️ Processando pedido...");
            Order order = jsonb.fromJson(payload, Order.class);

            if (new Random().nextInt(4) == 0) {
                throw new RuntimeException("Erro simulado no processamento");
            }

            System.out.println(" Pedido processado: " + order.toString());
            simulateProcessing();

        } catch (Exception e) {
            System.err.println("❌ Erro ao processar o pedido: " + e.getMessage());
            throw new RuntimeException("Falha no processamento", e);
        }
    }

    public void fallbackProcess(String payload) {
        System.err.println("⚠️ Fallback acionado no processamento: ");
        try (JMSContext context = connectionFactory.createContext()) {
            JMSProducer producer = context.createProducer();
            producer.send(context.createQueue("DLQ"), payload);
            System.out.println("Mensagem enviada para dead letter queue.");
        } catch (Exception e) {
            System.err.println("Falha ao enviar para dead letter queue: " + e.getMessage());
        }

    }

    private void simulateProcessing() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}