package factory;

import common.UseCaseNotification;

public class WhatsappNotificationFactory extends AbstractNotificationFactory {

    @Override
    public UseCaseNotification.PresenterNotification createPresenter() {
        return (message) -> System.out.printf("Enviando WHATSAPP: %s\n",
                message);
    }

}
