package banco;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Instant;

public class LoggingInvocationHandler implements InvocationHandler {

    private final Object objetoReal;

    public LoggingInvocationHandler(Object objetoReal) {
        this.objetoReal = objetoReal;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("### LOG INICIO ###");
        System.out.println(Instant.now() + ": Chamando método -> " + method.getName());

        if (args != null) {
            System.out.println("Com argumentos: " + java.util.Arrays.toString(args));
        }

        // Delega a chamada para o método do objeto real
        Object resultado = method.invoke(objetoReal, args);

        System.out.println(Instant.now() + ": Método " + method.getName() + " finalizado.");
        System.out.println("### LOG FIM ###");

        return resultado;

    }

}
