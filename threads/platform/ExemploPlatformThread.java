package platform;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExemploPlatformThread {

    // Exemplo de 8 tarefas de CPU (cálculo)

    public static void executarTarefasDeCPU() throws InterruptedException {

        int numeroDeNucleos = 4;
        System.out.println("== Exemplo: 8 Tarefas de CPU (cálculo) com PLATFORM THREADS (pool de 4) ==");
        System.out.println("Esperado: Apenas 4 tarefas começam. As outras 4 esperam na fila. Tempo total ~4 segundos.\n");

        Instant inicio = Instant.now();

        ExecutorService executor = Executors.newFixedThreadPool(numeroDeNucleos);
        try {
            for (int i = 1; i <= 8; i++) {
                int idTarefa = i;
                executor.submit(() -> tarefaDeCPU(idTarefa, inicio));
            }
        } finally {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
        }

        Instant fim = Instant.now();
        System.out.println("\n[Lote de CPU] Tempo total de execução: " + Duration.between(inicio, fim).toMillis() + " ms");
    }

    public static void tarefaDeCPU(int id, Instant inicioGlobal) {
        long tempoDesdeInicio = Duration.between(inicioGlobal, Instant.now()).toMillis();
        System.out.printf("CPU Tarefa #%d: INICIOU no momento %d ms. (Thread: %s)\n",
                id, tempoDesdeInicio, Thread.currentThread().getName());

        long fimDoCalculo = System.currentTimeMillis() + 2000;
        while (System.currentTimeMillis() < fimDoCalculo) {
            // Laço vazio para rodar CPU
        }

        tempoDesdeInicio = Duration.between(inicioGlobal, Instant.now()).toMillis();
        System.out.printf("CPU Tarefa #%d: FINALIZOU no momento %d ms.\n", id, tempoDesdeInicio);
    }
}
