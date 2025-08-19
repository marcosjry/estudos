package teste.kafka.producer.web.adapter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import teste.kafka.producer.application.port.out.OrderProducerPort;
import teste.kafka.producer.domain.model.Order;
import teste.kafka.producer.domain.model.OrderItem;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class OrderProducer implements OrderProducerPort {


    private static final Logger logger = LoggerFactory.getLogger(OrderProducer.class);

    @Inject
    @Channel("orders")
    private Emitter<String> emitter;

    private AtomicInteger attempt = new AtomicInteger(0);
    private final AtomicInteger clientId = new AtomicInteger(0);

    @Retry(maxRetries = 3, delay = 1000)
    @Timeout(2000)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000)
    @Fallback(fallbackMethod = "fallbackSend")
    @Override
    public Order send(Order order, String typeError) {
        order.deliver();
        CountDownLatch latch = new CountDownLatch(order.getItems().size());

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (OrderItem oI : order.getItems()) {

                final int currentClientId = clientId.incrementAndGet();

                executor.submit(() -> {
                    try {
                        String payload = JsonbBuilder.create().toJson(oI);
                        logger.info("Cliente {} enviando item {} para Kafka", currentClientId, payload);
                        emitter.send(payload);
                        logger.info("Cliente {} concluiu envio do item com sucesso", currentClientId);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
        } catch (InterruptedException e) {
            logger.error("Interrupção durante o envio de mensagens", e);
            Thread.currentThread().interrupt();
        }

        logger.info("✅ Pedido completo com {} itens enviado para Kafka", order.getItems().size());
        attempt.set(0);
        return order;
    }

    @Override
    public Order fallbackSend(Order order, String typeError) {
        System.out.println("Erro ao enviar pedido para fila.\n");
        throw new RuntimeException("Pedido cancelado devido a falha no envio.");
    }

}
