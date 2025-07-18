package pattern.proxy;

import common.UseCaseNotification;

public class TransactionalUseCaseProxy implements UseCaseNotification {

    private final UseCaseNotification useCase;

    public TransactionalUseCaseProxy(UseCaseNotification useCase) {
        this.useCase = useCase;
    }

    @Override
    public void notifyEveryHour(String customerId, PresenterNotification presenter, int identifier) {

        System.out.printf("Iniciando execução do método %s.notifyEveryHour\n",
                useCase.getClass().getSimpleName());

        String statusFinal = "sucesso";
        try {
            useCase.notifyEveryHour(customerId, presenter, identifier);
        } catch (Exception e) {
            statusFinal = "erro";
            System.out.println(">>> Ocorreu um erro: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            System.out.printf("Finalizando execução do método %s.notifyEveryHour com %s\n",
                    useCase.getClass().getSimpleName(), statusFinal);
        }
    }

}
