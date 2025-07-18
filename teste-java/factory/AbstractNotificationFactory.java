package factory;

import common.PoolingUseCaseNotification;
import common.UseCaseNotification;
import dinamic.proxy.TransactionalInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public abstract class AbstractNotificationFactory implements NotificationFactory {

    @Override
    public UseCaseNotification createUseCase() {
        UseCaseNotification useCaseReal = new PoolingUseCaseNotification();

        InvocationHandler handler = new TransactionalInvocationHandler(useCaseReal);

        return (UseCaseNotification) Proxy.newProxyInstance(
                UseCaseNotification.class.getClassLoader(),
                new Class<?>[]{ UseCaseNotification.class },
                handler
        );
    }

    @Override
    public abstract UseCaseNotification.PresenterNotification createPresenter();

}
