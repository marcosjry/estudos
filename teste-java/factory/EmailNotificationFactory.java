package factory;

import common.UseCaseNotification;

public class EmailNotificationFactory extends AbstractNotificationFactory {

    @Override
    public UseCaseNotification.PresenterNotification createPresenter() {
        return (message) -> System.out.printf("Enviando E-MAIL: %s\n", message);
    }

}
