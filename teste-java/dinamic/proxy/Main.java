package dinamic.proxy;

import common.UseCaseNotification;
import factory.EmailNotificationFactory;
import factory.NotificationFactory;
import factory.SMSNotificationFactory;
import factory.WhatsappNotificationFactory;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        System.out.println(">>> Iniciando agendador de notificações com Abstract Factory. <<<");

        List<NotificationFactory> factories = List.of(
                new EmailNotificationFactory(),
                new SMSNotificationFactory(),
                new WhatsappNotificationFactory()
        );

        ScheduledExecutorService controller = Executors.newSingleThreadScheduledExecutor();

        controller.scheduleAtFixedRate(() -> {

            System.out.println("\n### Nova Execução Agendada ###");

            try {

                var nextPos = new Random().nextInt(factories.size());

                NotificationFactory selectedFactory = factories.get(nextPos);
                //System.out.println("Fábrica selecionada: " + selectedFactory.getClass().getSimpleName());

                UseCaseNotification useCase = selectedFactory.createUseCase();
                UseCaseNotification.PresenterNotification presenter = selectedFactory.createPresenter();

                useCase.notifyEveryHour(UUID.randomUUID().toString(), presenter, nextPos);

            } catch (RuntimeException e) {
                controller.shutdown();
            }
        }, 1, 2, TimeUnit.SECONDS);
    }
}
