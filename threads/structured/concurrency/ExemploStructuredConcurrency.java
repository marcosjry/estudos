package structured.concurrency;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class ExemploStructuredConcurrency {

    public static String montarPainel(long userId) throws InterruptedException {

        Instant inicio = Instant.now();

        try (ExecutorService executor = Executors.newFixedThreadPool(3)) {


            Future<String> userFuture = executor.submit(() -> findUser(userId, inicio));
            Future<String> ordersFuture = executor.submit(() -> fetchOrders(userId, inicio));
            Future<String> recsFuture = executor.submit(() -> fetchRecommendations(userId, inicio));

            String userData = null;
            String ordersData = null;

            try {

                userData = userFuture.get();
                ordersData = ordersFuture.get();

                String recsData = recsFuture.get();
                return "Painel: " + userData + ", " + ordersData + ", " + recsData;

            } catch (ExecutionException e) {
                System.out.println("ERRO: Uma das tarefas falhou: " + e.getCause().getMessage());
                System.out.println("Cancelando outras tarefas...");
                userFuture.cancel(true);
                ordersFuture.cancel(true);
                throw new RuntimeException("Falha ao montar painel.", e.getCause());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread principal interrompida.", e);
            } finally {
                executor.shutdown();
                executor.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }

    public static String montarPainelStructuredTask(long userId) throws InterruptedException, ExecutionException {

        Instant inicio = Instant.now();
        // 1. O escopo define a política: se uma falhar, todas as outras são canceladas.
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            // 2. Bifurcamos (fork) todas as tarefas. Elas começam a rodar em Virtual Threads.
            Supplier<String> userSupplier = scope.fork(() -> findUser(userId, inicio));
            Supplier<String> ordersSupplier = scope.fork(() -> fetchOrders(userId, inicio));
            Supplier<String> recsSupplier = scope.fork(() -> fetchRecommendations(userId, inicio));

            // 3. Esperamos que todas terminem (ou que uma falhe)
            scope.join();

            // 4. Se alguma tarefa falhou, lança a exceção dela. O escopo já cancelou as outras.
            scope.throwIfFailed();

            // 5. Se chegamos aqui, TUDO deu certo. Coletamos os resultados.
            return "Painel: " + userSupplier.get() + ", " + ordersSupplier.get() + ", " + recsSupplier.get();
        }
        // O `catch` para a exceção de `throwIfFailed()` ficaria aqui fora, no chamador.
    }

    public static void exibeThreadInfo(Instant inicio) {
        long tempoDesdeInicio = Duration.between(inicio, Instant.now()).toMillis();
        System.out.printf("\nThread atual: %s, Inicio: %d ms", Thread.currentThread().getName(), tempoDesdeInicio);
    }

    private static String findUser(long id, Instant inicio) throws InterruptedException {
        exibeThreadInfo(inicio);
        System.out.println("Buscando usuário...");
        Thread.sleep(1000);
        System.out.println("Usuário encontrado.");
        return "Dados do Usuário " + id;
    }

    private static String fetchOrders(long id, Instant inicio) throws InterruptedException {
        exibeThreadInfo(inicio);
        System.out.println("Buscando pedidos...");
        Thread.sleep(2000);
        System.out.println("Pedidos encontrados.");
        return "5 Pedidos para o Usuário " + id;
    }

    private static String fetchRecommendations(long id, Instant inicio) throws InterruptedException {
        exibeThreadInfo(inicio);
        System.out.println("Buscando recomendações...");
        Thread.sleep(500);
        throw new RuntimeException("API de Recomendações indisponível");
    }
}
