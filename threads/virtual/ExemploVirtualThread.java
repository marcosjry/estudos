package virtual;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExemploVirtualThread {

    // Exemplo com 16 tarefas de I/O (espera) com Virtual Threads

    public static void executarTarefasDeIO() throws InterruptedException {
        System.out.println("== Exemplo: 8 Tarefas de I/O (espera) com VIRTUAL THREADS ==");
        System.out.println("Esperado: Todas as 8 tarefas começam quase juntas. Tempo total ~2 segundos.\n");

        Instant inicio = Instant.now();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 1; i <= 16; i++) {
                int idTarefa = i;
                executor.submit(() -> {
                    // Como se trata de Virtual Threads, não precisamos nos preocupar com o número de núcleos.
                    // Elas podem ser executadas concorrentemente, mesmo que sejam muitas.
                    // E por padrão não possuem nome.
                    String nomeThread = Thread.currentThread().getName();
                    tarefaDeIO(idTarefa, inicio, nomeThread);
                });
            }
        }

        Instant fim = Instant.now();
        System.out.println("\n[Lote de I/O] Tempo total de execução: " + Duration.between(inicio, fim).toMillis() + " ms");
    }

    public static void tarefaDeIO(int id, Instant inicioGlobal, String nomeThread) {
        long tempoDesdeInicio = Duration.between(inicioGlobal, Instant.now()).toMillis();
        System.out.printf("I/O Tarefa #%d: INICIOU no momento %d ms. (Thread: %s)\n",
                id, tempoDesdeInicio, nomeThread);
        try {
            Thread.sleep(Duration.ofSeconds(2));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        tempoDesdeInicio = Duration.between(inicioGlobal, Instant.now()).toMillis();
        System.out.printf("I/O Tarefa #%d: FINALIZOU no momento %d ms.\n", id, tempoDesdeInicio);
    }
}
