package common;

public interface UseCaseNotification {

    void notifyEveryHour(String customerId, PresenterNotification presenter, int identifier);

    @FunctionalInterface
    interface PresenterNotification {
        void notification(String message);
    }

}
