package com.gubee.poc.infrastructure.web.adapter;

import com.gubee.poc.domain.model.Order;
import com.gubee.poc.application.port.out.OrderSenderPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.json.bind.JsonbBuilder;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class JmsOrderSender implements OrderSenderPort {

    @Inject
    ConnectionFactory connectionFactory;

    private AtomicInteger attempt = new AtomicInteger(0);

    @Retry(maxRetries = 3, delay = 1000)
    @Timeout(2000)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    @Fallback(fallbackMethod = "fallbackSend")
    @Override
    public Order send(Order order, String typeError) {
        switch (typeError) {
            case "1":
                int count = attempt.incrementAndGet();
                System.out.println("Enviando pedido (Retry). Tentativa #" + count);
                if (count < 3) throw new RuntimeException("Erro simulado para Retry");
                break;

            case "2":
                System.out.println("Enviando pedido (Timeout)");
                try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
                break;

            default:
                System.out.println("Enviando pedido (Erro Permanente)");
                throw new RuntimeException("Erro permanente simulado");
        }

        try (JMSContext context = connectionFactory.createContext()) {
            order.deliver();
            JMSProducer producer = context.createProducer();
            String payload = JsonbBuilder.create().toJson(order);
            producer.send(context.createQueue("queue.orders"), payload);
            System.out.println("✅ Pedido enviado: " + order);
            attempt.set(0);
        }
        return order;
    }

    @Override
    public Order fallbackSend(Order order, String typeError) {
        System.out.println("Erro ao enviar pedido para fila.\n");
        throw new RuntimeException("Pedido cancelado devido a falha no envio.");
    }
}
