package pattern.proxy;

import common.PoolingUseCaseNotification;
import common.UseCaseNotification;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        UseCaseNotification useCaseReal = new PoolingUseCaseNotification();

        UseCaseNotification useCaseProxy = new TransactionalUseCaseProxy(useCaseReal);

        ScheduledExecutorService controller = Executors.newSingleThreadScheduledExecutor();

        UseCaseNotification.PresenterNotification emailPresenter = (message) -> System.out.printf("Enviando E-MAIL: %s\n", message);
        UseCaseNotification.PresenterNotification whatsAppPresenter = (message) -> System.out.printf("Enviando WHATSAPP: %s\n", message);
        UseCaseNotification.PresenterNotification smsPresenter = (message) -> System.out.printf("Enviando SMS: %s\n", message);

        UseCaseNotification.PresenterNotification[] notifications2 = { emailPresenter, whatsAppPresenter, smsPresenter };

        System.out.println(">>> Iniciando agendador de notificações <<<");

        controller.scheduleAtFixedRate(() -> {
            System.out.println("\n--- Nova Execução Agendada ---");
            var nextPos = new Random().nextInt(3);

            try {
                useCaseProxy.notifyEveryHour(UUID.randomUUID().toString(), notifications2[nextPos], nextPos);
            } catch ( RuntimeException e) {
                controller.shutdown();
            }

        }, 1, 2, TimeUnit.SECONDS);
    }
}
