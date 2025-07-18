package factory;

import common.UseCaseNotification;

public interface NotificationFactory {

    UseCaseNotification createUseCase();
    UseCaseNotification.PresenterNotification createPresenter();

}
