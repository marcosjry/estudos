package factory;

import common.UseCaseNotification;

public class SMSNotificationFactory extends AbstractNotificationFactory {

    @Override
    public UseCaseNotification.PresenterNotification createPresenter() {
        return (message) -> System.out.printf("Enviando SMS: %s\n", message);
    }
}
