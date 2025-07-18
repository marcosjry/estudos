package common;

import java.util.Random;

public class PoolingUseCaseNotification implements UseCaseNotification {

    @Transaction
    @Override
    public void notifyEveryHour(String customerId, PresenterNotification presenter, int identifier) {
        System.out.println("processando regra de negócio para notificação...");

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        presenter.notification(String.format("mensagem a ser enviada para %s: %s", customerId, new Random().nextInt()));

        if (identifier == 2) throw new RuntimeException("### Simulando falha na notificação ###");
    }
}
