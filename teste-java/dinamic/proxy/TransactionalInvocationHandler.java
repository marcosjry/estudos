package dinamic.proxy;

import common.Transaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TransactionalInvocationHandler implements InvocationHandler {

    private final Object objetoReal;

    public TransactionalInvocationHandler(Object objetoReal) {
        this.objetoReal = objetoReal;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Method implMethod = objetoReal.getClass().getMethod(method.getName(), method.getParameterTypes());

        if (implMethod.isAnnotationPresent(Transaction.class)) {
            System.out.printf("Iniciando execução do método $%s.%s\n",
                    method.getName(), objetoReal.getClass().getSimpleName());

            String statusFinal = "sucesso";
            Object resultado = null;
            try {
                resultado = method.invoke(objetoReal, args);
            } catch (Exception e) {
                statusFinal = "erro";
                Throwable causaReal = e.getCause() != null ? e.getCause() : e;
                System.out.println("xxx Ocorreu um erro: " + causaReal.getMessage());
                throw causaReal;
            } finally {
                System.out.printf("Finalizando execução do método %s.%s com %s\n",
                        method.getName(), objetoReal.getClass().getSimpleName(), statusFinal);
            }
            return resultado;
        } else {
            return method.invoke(objetoReal, args);
        }
    }
}
